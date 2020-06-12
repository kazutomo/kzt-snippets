// naive daxpy implementation
#include <CL/sycl.hpp>

using namespace std;
//using namespace cl::sycl;
using namespace sycl;

template <typename FPType>
static void runkernel(void)
{
    constexpr size_t nelems = 1<<27;
    vector<FPType> x(nelems);
    vector<FPType> y(nelems);
    FPType A = 2.0;

    queue q;

    for(int i = 0; i < nelems; i++) {
	y[i] = 0.0;
	x[i] = i;
    }

    buffer<FPType> dx {x.data(), nelems};
    {
      buffer<FPType> dy {y.data(), nelems}; // here because the desctructor copies data back to y

	q.submit([&](sycl::handler& h) {
		auto kx = dx.template get_access<access::mode::read>(h);
		auto ky = dy.template get_access<access::mode::read_write>(h);

		h.parallel_for<class daxpy>(range<1> {nelems}, [=] (id<1> idx) {
			ky[idx] += A * kx[idx];
		    });
	    });
    }

    int failed = 0;
    for (size_t i=0; i< nelems; ++i ) {
	FPType d =  fabs(y[i] - (A*i));
	if (d != 0.0) {
	    failed ++;
	}
    }
    if (failed) cout << "Failed to verify! failed=" << failed << std::endl;
    else cout << "Passed!" << std::endl;
}

int main()
{
    runkernel<float>();

    return 0;
}
