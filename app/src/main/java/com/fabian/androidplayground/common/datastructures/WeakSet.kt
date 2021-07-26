package com.fabian.androidplayground.common.datastructures

/**
 * A weak Set. An element stored in the WeakSet might be
 * garbage collected, if there is no strong reference to this element.
 */
class WeakSet<T> : Set<T> by WeakMutableSet()