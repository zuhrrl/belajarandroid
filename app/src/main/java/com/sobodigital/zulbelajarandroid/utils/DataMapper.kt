package com.sobodigital.zulbelajarandroid.utils

object DataMapper {


    fun<T,R> mapEntityToDomain(input: T, mapper: (T) -> R) : R {
        return mapper(input)
    }

    fun<T,R> mapDomainToEntity(input: T, mapper: (T) -> R) : R {
        return mapper(input)
    }
}