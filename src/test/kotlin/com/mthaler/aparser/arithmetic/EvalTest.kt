package com.mthaler.aparser.arithmetic

import com.mthaler.aparser.util.Result
import com.mthaler.aparser.util.roundDouble
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.PI

class EvalTest: StringSpec({

    "expression" {
        val e = Expression
        e("3.14").eval() shouldBe Result.OK(3.14, "")
        e("-3.14").eval() shouldBe Result.OK(-3.14, "")
        e("10e3").eval() shouldBe Result.OK(10000.0, "")
        e("3+4").eval() shouldBe Result.OK(7.0, "")
        e("3 + 4").eval() shouldBe Result.OK(7.0, "")
        e("-3 + 4").eval() shouldBe Result.OK(1.0, "")
        e("3 - 4").eval() shouldBe Result.OK(-1.0, "")
        e("3 + (4 + 5)").eval() shouldBe Result.OK(12.0, "")
        e("(3 + 4) + 5").eval() shouldBe Result.OK(12.0, "")
        e("3 * 4").eval() shouldBe Result.OK(12.0, "")
        e("3 * 4 + 2").eval() shouldBe Result.OK(14.0, "")
        e("2 + 3 * 4").eval() shouldBe Result.OK(14.0, "")
        e("2 ^ 3").eval() shouldBe Result.OK(8.0, "")
        e("sin(0)").eval() shouldBe Result.OK(0.0, "")
        e("cos(0)").eval() shouldBe Result.OK(1.0, "")
        e("4^3^2").eval() shouldBe Result.OK(262144.0, "")
        e("sqrt(2 + 2)").eval() shouldBe Result.OK(2.0, "")
        e("\u221A(2 + 2)").eval() shouldBe Result.OK(2.0, "")
        e("sin(pi / 2)").eval() shouldBe Result.OK(1.0, "")
        e("sin(\u03C0 / 2)").eval() shouldBe Result.OK(1.0, "")
        e("ln(e)").eval() shouldBe Result.OK(1.0, "")
        e("ln(e * e)").eval() shouldBe Result.OK(2.0, "")
        e("exp(1)").eval() shouldBe Result.OK(Math.E, "")
        e("A + 3").eval(Context(TrigonometricUnit.Rad, mapOf(Pair("A", 2.0)))) shouldBe Result.OK(5.0, "")
        shouldThrow<UndefinedVariableException> {
            e("A + 3").eval()
        }
    }

    "constantsExpressions" {
        val e = Expression
        e("pi").eval() shouldBe Result.OK(PI, "")
        e("\u03C0").eval() shouldBe Result.OK(PI, "")
    }

    "trigonometricExpression" {
        val e = Expression
        // radians
        e("sin(0)").eval() shouldBe Result.OK(0.0, "")
        e("cos(0)").eval() shouldBe Result.OK(1.0, "")
        e("tan(0)").eval() shouldBe Result.OK(0.0, "")
        e("sin(${PI / 2})").eval() shouldBe Result.OK(1.0, "")
        e("cos(${PI / 2})").eval().map { roundDouble(it, 6) } shouldBe Result.OK(0.0, "")
        // degrees
        val ctx = Context.Empty.copy(trigonometricUnit = TrigonometricUnit.Degree)
        e("sin(0)").eval(ctx) shouldBe Result.OK(0.0, "")
        e("cos(0)").eval(ctx) shouldBe Result.OK(1.0, "")
        e("tan(0)").eval(ctx) shouldBe Result.OK(0.0, "")
        e("sin(90)").eval(ctx) shouldBe Result.OK(1.0, "")
        e("cos(90)").eval(ctx).map { roundDouble(it, 6) } shouldBe Result.OK(0.0, "")
    }

    "inverseTrigonometricExpression" {
        val e = Expression
        // radians
        e("asin(0)").eval() shouldBe Result.OK(0.0, "")
        e("acos(1.0)").eval() shouldBe Result.OK(0.0, "")
        e("atan(0)").eval() shouldBe Result.OK(0.0, "")
        e("asin(1)").eval() shouldBe Result.OK(PI / 2.0, "")
        e("acos(0)").eval() shouldBe Result.OK(PI / 2.0, "")
        // degrees
        val ctx = Context.Empty.copy(trigonometricUnit = TrigonometricUnit.Degree)
        e("asin(0)").eval(ctx) shouldBe Result.OK(0.0, "")
        e("acos(1.0)").eval(ctx) shouldBe Result.OK(0.0, "")
        e("atan(0)").eval(ctx) shouldBe Result.OK(0.0, "")
        e("asin(1)").eval(ctx) shouldBe Result.OK(90.0, "")
        e("acos(0)").eval(ctx) shouldBe Result.OK(90.0, "")
    }

    "tryEval" {
        val e = Expression
        e("42").tryEval() shouldBe kotlin.Result.success(42.0)
        e("3+4").tryEval() shouldBe kotlin.Result.success(7.0)
        e("sin(0)").tryEval() shouldBe kotlin.Result.success(0.0)
    }
})