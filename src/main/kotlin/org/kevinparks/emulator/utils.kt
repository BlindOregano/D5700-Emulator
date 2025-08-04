package org.kevinparks.emulator

import java.io.File

fun paddedROM(vararg bytes: UByte): UByteArray {
    val rom = UByteArray(4096) { 0u }
    for (i in bytes.indices) {
        rom[i] = bytes[i]
    }
    return rom
}

fun loadROMFromFile(path: String): UByteArray {
    val file = File(path)
    val bytes = file.readBytes()
    val rom = UByteArray(4096) { 0u }

    for (i in bytes.indices) {
        if (i >= rom.size) break
        rom[i] = bytes[i].toUByte()
    }
    return rom
}


fun printRegisterState(cpu: D5700CPU) {
    println("Registers:")
    cpu.registers.forEachIndexed { i, value ->
        println("  r$i = 0x${value.toString(16).padStart(2, '0').uppercase()} (${value.toInt()})")
    }
    println("  P = 0x${cpu.programCounter.toString(16).padStart(4, '0').uppercase()}")
    println("  A = 0x${cpu.address.toString(16).padStart(4, '0').uppercase()}")
    println("  T = ${cpu.timer}")
    println("  M = ${if (cpu.memoryFlag) "ROM" else "RAM"}")
    println()
}
