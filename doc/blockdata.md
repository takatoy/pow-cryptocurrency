# Block

Block structure is shown below and it is mostly imitating Bitcoin's block structure.

|Field                  |Description                                    |Size           |
|:----------------------|:----------------------------------------------|:--------------|
|Magic no               |value always 0xA46120EF                        |4 bytes        |
|Blocksize              |number of bytes following up to end of block   |4 bytes        |
|Blockheader            |consists of 6 items                            |80 bytes       |
|Transaction counter    |positive integer                               |1 - 9 bytes    |
|transactions           |list of transactions                           |many           |

## Block Header

Block header structure is shown below.

|Field              |Purpose                                                        |Size       |
|:------------------|:--------------------------------------------------------------|:----------|
|Version            |Block Version Number                                           |4 bytes    |
|hashPrevBlock      |256-bit hash of the previous block header                      |32 bytes   |
|hashMerkleRoot     |256-bit hash based on all of the transactions in the block     |32 bytes   |
|Time               |Current timestamp as seconds since 1970-01-01 T00:00 UTC       |4 bytes    |
|Bits               |Current target in compact format                               |4 bytes    |
|Nonce              |32-bit number (starts at 0)                                    |4 bytes    |
