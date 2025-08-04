package org.kevinparks.emulator

import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.display.Display
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    print("Enter program file path: ")
    val path = readLine()?.trim()

    if (path.isNullOrEmpty()) {
        println("No path provided. Exiting.")
        return
    }

    val rom = loadROMFromFile(path)
    val memory = Memory(rom)
    val display = Display()
    val cpu = D5700CPU(memory, display)

    println("First 10 bytes of ROM:")
    rom.take(10).forEachIndexed { i, byte ->
        println("[$i] = ${byte.toString(16).padStart(2, '0').uppercase()}")
    }

    val executor = Executors.newScheduledThreadPool(2)

    val running = AtomicInteger(1)

    val cpuFuture = executor.scheduleAtFixedRate({
        if (running.get() == 0) return@scheduleAtFixedRate

        val pc = cpu.programCounter.toInt()
        val instruction = memory.readROMInstruction(pc)

        if (instruction == 0x0000.toUShort()) {
            println("Program halted (0000 instruction).")
            running.set(0)
            return@scheduleAtFixedRate
        }

//        println("Executing: [0x${pc.toString(16).padStart(4, '0').uppercase()}] = 0x${instruction.toString(16).padStart(4, '0').uppercase()}")
        cpu.step()
//        cpu.drawTimerValue()
//        printRegisterState(cpu)

    }, 0, 2, TimeUnit.MILLISECONDS)

    // Timer task (60Hz = every 16.67ms)
    val timerFuture = executor.scheduleAtFixedRate({
        if (running.get() == 0) return@scheduleAtFixedRate

        if (cpu.timer > 0u) {
            cpu.timer = (cpu.timer - 1u).toUByte()

            if (cpu.hasTimerChanged()) {
                cpu.display.render()
            }
        }
    }, 0, 1000L / 60L, TimeUnit.MILLISECONDS)

    // Await termination
    while (running.get() == 1) {
        Thread.sleep(50)
    }

    // Clean up
    cpuFuture.cancel(true)
    timerFuture.cancel(true)
    executor.shutdown()


//    while (true) {
//        val pc = cpu.programCounter.toInt()
//        val instruction = memory.readROMInstruction(pc)
//
//        if (instruction == 0x0000.toUShort()) {
//            println("Program halted (0000 instruction).")
//            break
//        }
//
//        println("Executing: [0x${pc.toString(16).padStart(4, '0').uppercase()}] = 0x${instruction.toString(16).padStart(4, '0').uppercase()}")
//        cpu.step()
//        printRegisterState(cpu)
//    }

    println("\nFinal Screen:")
    display.render()
}
