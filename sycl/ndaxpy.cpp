// naive daxpy implementation

#include <CL/sycl.hpp>

using namespace std;
//using namespace cl::sycl;
using namespace sycl;


static void runkernel()
{
    constexpr size_t nelems = 1<<27;
    vector<double> x(nelems);
    vector<double> y(nelems);
    double A = 2.0;

    queue q;

    for(int i = 0; i < nelems; i++) {
	y[i] = 100.0;
	x[i] = i;
    }

    buffer<double> dx {x.data(), nelems};

    {
	buffer<double> dy {y.data(), nelems}; // here because the desctructor copies data back to y

	q.submit([&](sycl::handler& h) {
		auto kx = dx.get_access<access::mode::read>(h);
		auto ky = dy.get_access<access::mode::read_write>(h);

		h.parallel_for<class daxpy>(range<1> {nelems}, [=] (id<1> idx) {
			ky[idx] += A * kx[idx];
		    });
	    });
    }

    int failed = 0;
    for (size_t i=0; i< nelems; ++i ) {
	if (y[i] != (100.0 + A*i)) failed ++;
    }
    if (failed) cout << "Failed to verify!" << std::endl;
    else cout << "Passed!" << std::endl;
}

int main()
{
    runkernel();
}
