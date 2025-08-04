package org.kevinparks.emulator.instructions

object InstructionFactory {
    fun decode(bytes: UShort): Instruction {
        val opcode = (bytes.toInt() ushr 12) and 0xF
        return when (opcode) {
            0x0 -> StoreInstruction(bytes)
            0x1 -> AddInstruction(bytes)
            0x2 -> SubInstruction(bytes)
            0x3 -> ReadInstruction(bytes)
            0x4 -> WriteInstruction(bytes)
            0x5 -> JumpInstruction(bytes)
            0x6 -> ReadKeyboardInstruction(bytes)
            0x7 -> if (bytes == 0x7000.toUShort()) SwitchMemoryInstruction() else throw IllegalArgumentException("Malformed SWITCH_MEMORY: opcode: $bytes")
            0x8 -> SkipEqualInstruction(bytes)
            0x9 -> SkipNotEqualInstruction(bytes)
            0xA -> SetAInstruction(bytes)
            0xB -> SetTimerInstruction(bytes)
            0xC -> ReadTimerInstruction(bytes)
            0xD -> ConvertToBase10Instruction(bytes)
            0xE -> ConvertByteToAsciiInstruction(bytes)
            0xF -> DrawInstruction(bytes)
            else -> throw IllegalArgumentException("Unknown opcode: $opcode")
        }
    }
}
