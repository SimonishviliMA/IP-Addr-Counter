# Fast start
## Mandatory
  You should add absolute path to your IPv4 file in config file. Use for it `file.name` property key.
## Playing
  You can configurate quantity of threads which will be reading your file and summing unique IPs. Use for it `threads.quantity` property key.
## My results
  4 additionaly threads (not including Main) gave me the following result. <br/>
  Complexity was ~10 minutes and didn't consume more than 1gb RAM.
## How it works
  * Separates file and run services for every thread (not including Main).
  * Assemblies IP by bytes and presentes to decimal number system. 
  * Takes that number as index for BitSet.
  * Returnes cardinality of all unique IPs.
