package com.domain.model.mapper

interface DomainMapper  <T, DomainModel>{
    fun mapToDomainModel(model: T): DomainModel
}