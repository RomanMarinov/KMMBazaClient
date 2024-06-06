package util

//import androidx.annotation.MainThread
//import androidx.lifecycle.LifecycleOwner
//import kotlinx.atomicfu.AtomicBoolean

//class SingleLiveEvent <T> : MutableLiveData<T>() {
//
//    private val pending = AtomicBoolean(false)
//
//    @MainThread
//    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
//        // Соблюдайте внутренние MutableLiveData
//        super.observe(owner, Observer<T> { t ->
//            if (pending.compareAndSet(true, false)) {
//                observer.onChanged(t)
//            }
//        })
//    }
//
//    @MainThread
//    override fun setValue(t: T?) {
//        pending.set(true)
//        super.setValue(t)
//    }
//
//    @MainThread
//    fun call() {
//        value = null
//    }
//
//}