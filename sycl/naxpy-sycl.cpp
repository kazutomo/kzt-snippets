// naive axpy implementation
#include <CL/sycl.hpp>
#include <sys/time.h>

using namespace std;
//using namespace cl::sycl;
using namespace sycl;

static double gettime(void)
{
  struct timeval tv;
  gettimeofday(&tv, 0);
  return (double)tv.tv_sec + (double)tv.tv_usec/1000.0/1000.0;
}

template <typename FPType>
static void runkernel(void)
{
    constexpr size_t nelems = 1<<26;
    vector<FPType> x(nelems);
    vector<FPType> y(nelems);
    FPType A = 2.0;


    cout << "nelems: " << nelems << std::endl;
    cout << "sizeof(FPType): " << sizeof(FPType) << std::endl;
    cout << "Memory [MB]: " << (sizeof(FPType) * nelems * 2)/1024./1024. << std::endl;

    //property_list prop{property::queue::enable_profiling()};
    //default_selector dsel{};
    //queue q(dsel, prop);

    queue q;

    for(int i = 0; i < nelems; i++) {
	y[i] = 0.0;
	x[i] = i;
    }

    buffer<FPType> dx {x.data(), nelems};
    double st = gettime();
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
    double et = gettime() - st;

    cout << "Runtime [Sec]: " << et << std::endl;

    cout << "GFlops : " << (double)nelems * 2.0 / et * 1e-9 << std::endl;

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
#if ENABLE_DP
    runkernel<double>();
#else
    runkernel<float>();
#endif
    
    return 0;
}
