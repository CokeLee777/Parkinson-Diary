const { JsonWebTokenError } = require("jsonwebtoken")

exports.InternalServerError = class InternalServerError extends Error {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_INTERNAL_SERVER'
    this.name = 'InternalServerError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.DatabaseConnectError = class DatabaseConnectError extends Error {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_DATABASE_CONNECT'
    this.name = 'DatabaseConnectError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.InvalidInputTypeError = class InvalidInputTypeError extends Error {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_INVALID_INPUT_TYPE'
    this.name = 'InvalidInputTypeError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.NotEnoughInputDataError = class NotEnoughInputDataError extends Error {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_INVALID_PATIENT_NUMBER'
    this.name = 'InvalidPatientNumberError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.BindingTokenError = class BindingTokenError extends JsonWebTokenError {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_BINDING_TOKEN'
    this.name = 'BindingTokenError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.InvalidTokenError = class InvalidTokenError extends JsonWebTokenError {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_INVALID_TOKEN'
    this.name = 'InvalidTokenError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

exports.TokenExpiredError = class TokenExpiredError extends JsonWebTokenError {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_TOKEN_EXPIRED'
    this.name = 'TokenExpiredError'
    this.stack = `${this.message}\n${super.stack}`
  }
}