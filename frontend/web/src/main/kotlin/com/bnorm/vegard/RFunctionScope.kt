package com.bnorm.vegard

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus
import react.useEffectWithCleanup
import react.useRef

fun useMainScope(
  name: String? = null
): CoroutineScope {
  val ref = useRef(if (name != null) MainScope() + CoroutineName(name) else MainScope())
  useEffectWithCleanup(listOf()) { return@useEffectWithCleanup { ref.current.cancel() } }
  return ref.current
}
