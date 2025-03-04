fun main() {
    val result1 = handleMethodCall { method1() }
    val result2 = handleMethodCall { method2() }
    val result3 = errhandle { sum(4, 6) }

    result1.onFailure { println("Error: ${it.message}") }
    result2.onSuccess { println("Success: $it") }
    result3.onSuccess { println(it) }
}

// 统一的错误处理方法
fun handleMethodCall(action: () -> String): Result<String> {
    return try {
        val result = action()
        Result.success(result) // 返回成功的结果
    } catch (e: Exception) {
        Result.failure(e) // 返回失败的异常
    }
}

fun errhandle(action: () -> Int): Result<Int> {
    return try {
        val result = action()
        Result.success(result) // 返回成功的结果
    } catch (e: Exception) {
        Result.failure(e) // 返回失败的异常
    }
}

// 方法1，可能抛出异常
fun method1(): String {
    throw Exception("Exception in method1")
}

// 方法2，正常执行
fun method2(): String {
    return "Method2 executed successfully"
}

fun sum(a: Int, b: Int): Int {
    return a + b;
}
