package com.example.demo.exception

import com.example.demo.common.ErrorResult
import com.example.demo.exception.exception.DatePastException
import com.example.demo.exception.exception.EmailDuplicateException
import com.example.demo.exception.exception.GameCardDuplicateException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeParseException

@ControllerAdvice
@RestController
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun validExceptionHandler(exception: MethodArgumentNotValidException): ErrorResult<List<String>> {
        val bindingResult = exception.bindingResult
        val resList = mutableListOf<String>()
        for (error in bindingResult.fieldErrors) {
            val tempMessage = """${error.defaultMessage} 값=[${error.rejectedValue}]"""
            resList.add(tempMessage)
        }
        log.warn("{}객체의 valid 실패 에러메시지=[{}]", bindingResult.objectName, resList)
        return ErrorResult(HttpStatus.BAD_REQUEST, resList)
    }

    @ExceptionHandler(EmailDuplicateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun emailDuplicateHandler(exception: EmailDuplicateException): ErrorResult<String> {
        log.warn("이메일중복발생 이메일={}", exception.email)
        return ErrorResult(HttpStatus.BAD_REQUEST, """${exception.msg} 값=[${exception.email}]""")
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun entityNotFoundHandler(exception: EntityNotFoundException): ErrorResult<String> {
        if (exception.message == null) {
            log.error("EntityNotFoundException에서 예외메시지없음 예외추적={}", exception.stackTraceToString())
            return ErrorResult(HttpStatus.BAD_REQUEST, "EntityNotFoundException이 발생했지만 message가 없습니다")
        }
        log.warn("엔티티id조회실패=메시지={}", exception.message!!)
        return ErrorResult(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun wrongEnumHandler(exception: IllegalArgumentException): ErrorResult<String> {
        log.warn("enum이 잘못들어옴")
        return ErrorResult(HttpStatus.BAD_REQUEST, "enum이 잘못되었습니다")
    }

    @ExceptionHandler(GameCardDuplicateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun wrongGameCardHandler(exception: GameCardDuplicateException): ErrorResult<String> {
        log.warn(
            "GameCard중복이 발생해서 insert에 실패함 게임={} 일련번호={} 메시지={}",
            exception.gameTitle,
            exception.serialNo,
            exception.msg,
        )
        return ErrorResult(
            HttpStatus.BAD_REQUEST,
            """GameCard중복이 발생했습니다 게임=${exception.gameTitle} 일련번호=${exception.serialNo}""",
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun requestBodyParsingFailHandler(
        req: HttpServletRequest,
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ErrorResult<String>> {
        val rootCause = exception.rootCause
        val requestUrl = req.requestURI
        if (rootCause is DateTimeParseException) {
            return invalidDateHandler(rootCause)
        }
        if (rootCause is DatePastException) {
            return wrongDateHandler(rootCause)
        }
        if (rootCause is JsonParseException) {
            return jsonParseFailHandler(rootCause, requestUrl)
        }
        if (rootCause is MissingKotlinParameterException) { // MismatchedInputException로 바뀔수있음
            return requestBodyParameterMissingHandler(rootCause, requestUrl)
        }
        if (rootCause == null) {
            return noRequestBodyHandler(requestUrl)
        }

        log.error(
            "HttpMessageNotReadableException 알려지지않은예외 url={} 루트예외={} 예외추적={}",
            requestUrl,
            exception.rootCause,
            exception.stackTraceToString(),
        )
        return ResponseEntity(
            ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, "알려지지않은예외"),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }

    fun invalidDateHandler(exception: DateTimeParseException): ResponseEntity<ErrorResult<String>> {
        log.warn("날짜파싱실패 파싱스트링={} 예외메시지={}", exception.parsedString, exception.message)
        val httpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity(
            ErrorResult(
                httpStatus,
                """날짜의 형식을 다시 확인해주세요 입력값=${exception.parsedString},바른형식=yyyymmdd""",
            ),
            httpStatus,
        )
    }

    @ExceptionHandler(DatePastException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun wrongDateHandler(exception: DatePastException): ResponseEntity<ErrorResult<String>> {
        log.warn("날짜가 과거1년이전으로 잘못들어옴 날짜={}", exception.date)
        val httpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity(ErrorResult(httpStatus, exception.msg), httpStatus)
    }

    fun jsonParseFailHandler(
        exception: JsonParseException,
        url: String,
    ): ResponseEntity<ErrorResult<String>> {
        log.error("requestBody의 파싱이 실패함 메시지={} url={}", exception.message, url)
        val httpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity(ErrorResult(httpStatus, "requestBody의 파싱이 실패함"), httpStatus)
    }

    fun requestBodyParameterMissingHandler(
        exception: MissingKotlinParameterException,
        url: String,
    ): ResponseEntity<ErrorResult<String>> {
        log.error(
            "requestBody의 파라미터가 잘못됨  파라미터={} url={} 메시지={}",
            exception.parameter,
            url,
            exception.message,
        )
        val httpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity(ErrorResult(httpStatus, "requestBody의 파라미터가 잘못됨"), httpStatus)
    }

    fun noRequestBodyHandler(url: String): ResponseEntity<ErrorResult<String>> {
        log.error("requestBody가 들어오지않음 url={}", url)
        val httpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity(ErrorResult(httpStatus, "requestBody가 들어오지않음"), httpStatus)
    }
}
