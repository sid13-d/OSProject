# Multiprogramming Operating System Project

## Project Overview

### Objectives

- **General Concepts**: Gain a comprehensive understanding of Multiprogramming Operating Systems.
- **Assembly Programming**: Learn how to write assembly programs for execution on a multiprogramming operating system.

## Introduction

This project involves the design and implementation of a Multiprogramming Operating System (MOS) for a hypothetical computer configuration, easily simulated (Shaw and Weiderman, 1971). The goal is to consolidate and apply concepts and techniques discussed in relevant literature, dealing directly with challenges in input-output, interrupt handling, process synchronization, scheduling, main and auxiliary storage management, process and resource data structures, and systems organization.

## Project Assumptions

We assume that the project will be coded for a large central computer facility (the "host" system). The host system ensures users cannot tamper with the operating system or machine resources. However, it provides a comprehensive set of services, including filing services, debugging aids, and a proficient higher-level language.

## Implementation Strategy

The global strategy is to simulate the hypothetical computer on the host and write the MOS for this simulated machine. The MOS and simulator are anticipated to consist of approximately 1000 to 1200 program cards, with the majority of the code representing the MOS. The project timeline is designed for completion over about two months, suitable for students concurrently taking a normal academic load.

# Machine Specifications

## The MOS Computer

The MOS computer is described from two perspectives: the "virtual" machine seen by the typical user and the "real" machine used by the MOS designer/implementer.

### a) The Virtual Machine

The virtual machine, as seen by a normal user, is illustrated in Fig. A-1. Key specifications include:

- **Storage**: Consists of a maximum of 100 words, addressed from 00 to 99. Each word is divided into four one-byte units, where a byte may contain any character acceptable by the host machine.

- **CPU Registers**:
  - **General Register (R)**: A four-byte general register.
  - **Boolean Toggle (C)**: A one-byte toggle that may contain either 'T' (true) or 'F' (false).
  - **Instruction Counter (IC)**: A two-byte instruction counter.

- **Word Interpretation**: A storage word may be interpreted as an instruction or data word. The operation code of an instruction occupies the two high-order bytes of the word, and the operand address appears in the two low-order bytes.

- **Instruction Format**: Table A-I gives the format and interpretation of each instruction. Notable instructions include:
  - **GD (Input)**: Reads only the first 40 columns of a card.
  - **PD (Output)**: Prints a new line of 40 characters.
  
- **Program Initialization**: The first instruction of a program must always appear in location 00.

- **Programming Characteristics**: With this simple machine, a batch of compute-bound, IO-bound, and balanced programs can be quickly written. The design intentionally allows for common programming errors, providing versatility in handling a variety of jobs and user errors.

- **Supervisor Storage**: Loosely defined as the amount of storage required for the MOS.

<!-- Insert an image or diagram illustrating the virtual machine -->
![Instruction Set](assets/Instruction%20set.png)

### b) The Real Machine

The "real" machine used by the MOS designer/implementer may have additional specifications beyond the user's view. These details are typically outlined in technical documentation provided for developers working on the MOS.

- **CPU Registers of Interest**:
  - **Boolean Toggle (C)**: A one-byte "Boolean" toggle.
  - **General Register (R)**: A four-byte general register.
  - **Instruction Counter (IC)**: A two-byte virtual machine location counter.
  - **Interrupt Registers**:
    - **Program Interrupt (PI)**
    - **System Interrupt (SI)**
    - **Timer Interrupt (TI)**
  - **Page Table Register (PTR)**: A four-byte page table register.
  - **Mode (MODE)**: Mode of CPU, either 'master' or 'slave'.
  
- **Storage**:
  - **User Storage**: Contains 300 four-byte words, addressed from 000 to 299. Divided into 30 ten-word blocks for paging purposes.
  - **Supervisor Storage**: Loosely defined as the amount of storage required for the MOS.

<!-- Insert an image or diagram illustrating the real machine -->
![Virtual User Machine](assets/virtual%20user%20machine.png)

### c) Slave Mode Operation

User storage addressing while in slave mode is accomplished through paging hardware. The PTR register contains the length and page table base location for the user process currently running. The four bytes a0 a1 a2, a3, in the PTR have this interpretation: a1 is the page table length minus 1, and l0a2, + a3, is the number of the user storage block in which the page table resides, where a1, a2, and a3 are digits.

A two-digit instruction or operand address, x1 x2, in virtual space is mapped by the relocation hardware into the real user storage address:
\[10 \times [10 \times (l0a2 + a3) + x1] + x2\]

Where (α) means "the contents of address" and it is assumed that \(x1 \leq a1\).

All pages of a process are required to be loaded into user storage prior to execution. It is assumed that each virtual machine instruction is emulated in one time unit. All interrupts occurring during slave mode operation are honored at the end of instruction cycles and cause a switch to master mode. The operations GD, PD, and H result in supervisor-type interrupt, that is, "supervisor calls." A program-type interrupt is triggered if the emulator receives an invalid operation code or if \(x1 > a1\) during the relocation map (invalid virtual space address).

### d) Master Mode Operation and Interrupt Handling

In master mode, the handling of interrupts is a crucial aspect of the MOS operation.

#### Interrupts

Three types of interrupts are possible:

1. **Program Interrupts**:
   - **Protection (Page Table Length)**
   - **Invalid Operand Code**
   - **Page Fault (PI=1, P1=2, PI=3)**

2. **Supervisor Interrupts**:
   - **GD (SI=1)**
   - **PD (SI=2)**
   - **H (SI=3)**

3. **Timer Interrupts**:
   - **Decrement to Zero (TI=2)**

The events causing interrupts of types (1) and (2) can happen only in slave mode. Events of type 3 can occur in both master and slave modes, and several of these events may happen simultaneously. The interrupt-causing event is recorded in the interrupt registers regardless of whether the interrupts are inhibited (master mode) or enabled (slave mode).

#### Handling of Interrupts

The interrupt registers are set by an interrupt event to the following values:

1. **Program Interrupt (PI)**:
   - \(PI=1\): Opcode
   - \(PI=2\): Invalid Operation Code
   - \(PI=3\): Page Fault

2. **Supervisor Interrupt (SI)**:
   - \(SI=1\): GD
   - \(SI=2\): PD
   - \(SI=3\): H

3. **Timer Interrupt (TI)**:
   - \(TI=2\): Timer

All interrupts occurring during slave mode operation are honored at the end of instruction cycles and cause a switch to master mode. The operations GD, PD, and H result in supervisor-type interrupts, that is, "supervisor calls." A program-type interrupt is triggered if the emulator receives an invalid operation code or if \(x1 > a1\) during the relocation map (invalid virtual space address).

### e) JOB, PROGRAM, AND DATA CARD FORMATS

A user job is submitted as a deck of control, program, and data cards in the order: `<JOB card>`, `<Program>`, `<DATA card>`, `<Data>`, `<ENDJOB card>`.

1. **`<JOB card>` Format:**
   - `$AMJ cc. 1-4`: A Multiprogramming Job
   - `<job Id> cc. 5—8`: A unique 4-character job identifier.
   - `<time estimate> cc. 9—12`: 4-digit maximum time estimate.
   - `<line estimate> cc. 13—16`: 4-digit maximum output estimate.

2. **`<Program>` Deck Format:**
   - Each card of the `<Program>` deck contains information in card columns 1-40.
   - The ith card contains the initial contents of user virtual memory locations: \(10 \times (i - 1), 10 \times (i - 1) + 1, \ldots, 10 \times (i - 1) + 9\), where \(i = 1, 2, \ldots, n\).
   - Each word may contain a VM instruction or four bytes of data.
   - The number of cards \(n\) in the program deck defines the size of the user space; i.e., \(n\) cards define \(10 \times n\) words, \(n \times 10\).

3. **`<DATA card>` Format:**
   - The `<DATA card>` has the format: The (Data) deck contains information in cc. 1—40 and is the user data retrieved by the VM GD instructions.

4. **`<ENDJOB card>` Format:**
   - The `<ENDJOB card>` has the format: `SEND cc. 1-4`.
   - `<job Id> cc. 5—8`: Same `<job Id>` as `<JOB card>`.
   - The `<DATA card>` is omitted if there are no `<Data>` cards in a job.

### f) Example Job Submission:

Here is an example of how a job can be submitted:

plaintext
$AMJ0010005001600
... (Program cards)
$DTA
... (Data cards)
$END0010005


Feel free to adjust the example and the formatting based on your specific needs or preferences.

# Multiprogramming Operating System Project

## Phase 1

### Algorithms
- [Algorithm for Phase 1](assets/AOSPROJECT1.pdf) - This file contains the algorithms implemented for Phase 1.

### Input Files
- [Input File for Phase 1](assets/input_Phase1.pdf) - This file displays the jobs written for Phase 1.

### Output Files
- [Output File for Phase 1](assets/output_Phase1.pdf) - This file contains the output of the jobs written in Phase 1.

## Phase 2

### Algorithms
- [Algorithm for Phase 2](assets/AOSPROJECT2.pdf) - This file contains the algorithms implemented for Phase 2.

### Input Files
- [Input File for Phase 2](assets/input_phase2.txt) - This file displays the jobs written for Phase 2.

### Output Files
- [Output File for Phase 2](assets/output_phase2.txt) - This file contains the output of the jobs written in Phase 2.

## Project Structure

For detailed information about the project structure, design, and other documentation, please refer to the appropriate directories in this repository.


## Getting Started

Follow these steps to get started with the project:

1. Clone the repository to your local machine.
2. Review the project requirements and documentation in the `docs` folder.
3. Set up the development environment as per the instructions in `setup.md`.
4. Explore the source code in the `src` directory to understand the implementation.

## Contributing

We welcome contributions to enhance the project. If you have suggestions, bug reports, or would like to contribute code, please follow our [contribution guidelines](CONTRIBUTING.md).

## License

This project is licensed under the [MIT License](LICENSE.md) - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

Special thanks to Shaw and Weiderman for their foundational work in this field.

