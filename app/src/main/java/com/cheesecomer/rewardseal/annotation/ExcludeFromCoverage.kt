package com.cheesecomer.rewardseal.annotation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcludeFromCoverage
