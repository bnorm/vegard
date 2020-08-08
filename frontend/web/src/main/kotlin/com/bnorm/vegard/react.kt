package com.bnorm.vegard

import react.RBuilder
import react.RMutableRef
import kotlin.reflect.KProperty

typealias RNode = RBuilder.() -> Unit

inline operator fun <T> RMutableRef<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
  return current
}

inline operator fun <T> RMutableRef<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
  current = value
}
