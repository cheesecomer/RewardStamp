package com.cheesecomer.rewardstamp.annotation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcludeFromCoverage
