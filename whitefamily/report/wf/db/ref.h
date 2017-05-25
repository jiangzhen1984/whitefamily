#pragma once
#include <atomic>

template struct __declspec(dllexport) ::std::atomic<int32_t>;

namespace db {
	class __declspec(dllexport) Ref {
	public:
		inline Ref() : mStrong(0){}
		inline Ref(Ref & rf) : mStrong(1){}
		~Ref(){  }

		void inc() { mStrong++; }
		int dec() { return --mStrong; }

		int refCount() { return mStrong; }

	private:
		::std::atomic<int32_t>    mStrong;
	};

	template <typename T>
	class __declspec(dllexport) SP {
	public:
		inline SP() :mRef(NULL){}
		SP(SP<T> &);
		SP(SP<T> const &);
		SP(T *);

		SP& operator =(T *);
		SP& operator =(SP<T> &);

		~SP();

		inline  T&      operator* () const  { return *mRef; }
		inline  T*      operator-> () const { return mRef; }
		inline  T*      get() const         { return mRef; }


	private:
		T * mRef;
	};




	template<typename T>
	SP<T>::SP(SP<T> & other) :mRef(other.mRef)
	{
		if (mRef)
		{
			mRef->inc();
		}
	}


	template<typename T>
	SP<T>::SP(SP<T> const & other) :mRef(other->mRef)
	{
		if (mRef)
		{
			mRef->inc();
		}
	}

	template<typename T>
	SP<T>::SP(T * other) :mRef(other)
	{
		if (other)
		{
			mRef->inc();
		}
	}
	template<typename T>
	SP<T>::~SP()
	{
		if (mRef)
		{
			int c = mRef->dec();
			if (c == 0)
			{
				delete mRef;
			}
		}
		mRef = NULL;
	}


	template<typename T>
	SP<T>& SP<T>::operator =(T * other)
	{
		if (mRef)
		{
			mRef->dec();
		}
		mRef = other;
		if (other)
		{
			other->inc();
		}
		return *this;
	}

	template<typename T>
	SP<T>& SP<T>::operator =(SP<T> & other)
	{
		if (mRef)
		{
			mRef->dec();
		}
		mRef = other.mRef;
		if (mRef)
		{
			mRef->inc();
		}
		return *this;
	}
}