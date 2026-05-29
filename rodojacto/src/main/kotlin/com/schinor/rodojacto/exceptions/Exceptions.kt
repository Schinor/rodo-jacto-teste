package com.schinor.rodojacto.exceptions

// Lança um erro 404 Not Found quando um ID não existe
class ResourceNotFoundException(message: String) : RuntimeException(message)

// Lança um erro 400 Bad Request para regras de negócio (ex: email já registado)
class BusinessRuleException(message: String) : RuntimeException(message)