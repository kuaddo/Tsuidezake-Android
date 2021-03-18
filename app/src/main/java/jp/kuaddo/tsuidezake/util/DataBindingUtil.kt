package jp.kuaddo.tsuidezake.util

import androidx.databinding.ViewDataBinding
import androidx.databinding.ViewStubProxy
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewDataBinding> viewStubDataBinding(
    viewStubProxyProvider: () -> ViewStubProxy
): ReadOnlyProperty<Fragment, T?> {
    return object : ReadOnlyProperty<Fragment, T?> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
            val viewStubProxy = viewStubProxyProvider()
            if (!viewStubProxy.isInflated) return null
            (viewStubProxy.root.getTag(property.name.hashCode()) as? T)?.let { return it }

            return (viewStubProxy.binding as? T)?.also {
                it.lifecycleOwner = thisRef.viewLifecycleOwner
                it.root.setTag(property.name.hashCode(), it)
            }
        }
    }
}
