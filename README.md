# HuffmanCompression

# Relevant files:
henc9169.java
hdec9169.java
Write.java
Read.java
CharacterCount.java

# Compilation instructions:

$ javac henc9169.java

$ javac hdec9169.java

# Instructions to run the program:

Encoding:

$ java henc9169 path_to_file_and_name

Decoding:

$ java hdec9169 path_to_file_and_name.huf

# Implementation and Limitations:
The file has been successfully tested for approximately 100MiB size on Unix based AFS of NJIT. However, if the size increases we encounter a java error.

The file has been successfully tested for following formats: .pdf, .docx, .jpg, .png, .mp4

The input file is automatically erased as specified in the instructions.
