package org.kevinparks.emulator

import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.InstructionFactory

class D5700CPU(
    internal val memory: Memory,
    internal val display: Display = Display(),
) {
    // 8 general-purpose 8-bit registers: r0 to r7
    val registers = UByteArray(8)

    // Special registers
    var programCounter: UShort = 0u      // P
    var timer: UByte = 0u                // T
    private var previousTimerValue: UByte = 0u
    var address: UShort = 0u             // A
    var memoryFlag: Boolean = false      // M: false = RAM, true = ROM

    fun reset() {
        programCounter = 0u
        timer = 0u
        previousTimerValue = 0u
        address = 0u
        memoryFlag = false
        for (i in registers.indices) {
            registers[i] = 0u
        }
    }

    fun step() {
        val high = memory.readByte(programCounter, true)
        val low = memory.readByte((programCounter + 1u).toUShort(), true)
        val instructionBytes = ((high.toInt() shl 8) or low.toInt()).toUShort()

        val instruction = InstructionFactory.decode(instructionBytes)
        instruction.execute(this)

        if (instruction.shouldAdvancePC) {
            programCounter = (programCounter + 2u).toUShort()
        }
    }

    fun hasTimerChanged(): Boolean {
        val changed = timer != previousTimerValue
        previousTimerValue = timer
        return changed
    }

}

