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

<!-- Insert an image or diagram (if available) illustrating the virtual machine -->

### b) The Real Machine

The "real" machine used by the MOS designer/implementer may have additional specifications beyond the user's view. These details are typically outlined in technical documentation provided for developers working on the MOS.

<!-- Include any additional details about the real machine used by the MOS designer/implementer -->
