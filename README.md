# Final Project: Distributed Crawler
## Project Overview
Distributed Crawler is an application that provides crawler
service to the client. \
On the client side, the client is asked to provide the URL
of the target web page to the agent. It will receive the crawl 
result from the agent server. \
On the server side, once one of the servers receives the request
from the client, it will initially decide which server will work 
on this task. During the crawling process, there will be some
algorithms be applied to solve collision and failures. Finally
the result will be returned to the client.

## Project Breakdown
The distributed crawler is consisted of two components: `crawler` 
and `storage`. If we view this project as a 
producer-consumer pattern application, the workflow of the 
project would be:
1. Crawler produces the crawl result.
2. Crawl results are stored in a result pool.
3. Messages inside the pool will consumed by the client through
RMI calls.


### Crawler
Initially, the client will send out request to the agent to 
launch the crawling process.\
The request is implemented as a `CrawlTask` in the resource files.
Client will be asked to construct such a CrawlTask object to be 
passed to the server through RMI.\
There will be multiple agents in the systems. The agent which 
received the request will make a decision on which server will 
deal with the crawl
request.\
The algorithm will be used to help the agent make the decision is
based on hashing. The algorithm will initially calculate the hash
value of the URL passed in. Since it knows the number of servers 
and it can assign serial number to those servers like 0,1,2...,
it can calculate the modulus of the hashed value 
(`Hash(URL) % # of servers`) to figure out to which server the
task should be assigned.
After one of the server received the task, it will work on the
crawling independently.
Finally, the crawling result will be sent to the storage.

### Storage
When the crawling is done, the result will be sent to the
database. The entry of the database will be organized by its
primary key -- task UUID.
The transactions between crawling service and storage
is also implemented by using RMI.
There are some rules be applied to optimize the performance:
1. When adding result to the database, the database will
check if the entry is already existed or not. If it is already
existed, the adding request will be ignored.
2. LRU Caching mechanic is introduced to keep the database in
a relatively small size.
3. Since database is partitioned, PAXOS algorithm is applied to
make servers be consistent with each other.

## Technical Impressions
### Crawler
The performance of crawler is not ideal especially crawling
pages with deeper depth. When the crawler is crawling for a
longer time, the client has to wait for a longer time. As a 
result, the client may have to make more effort to let the
waiting not disturb the other ongoing tasks. Currently we are
making the client to call `Crawl` and `RetrieveResult`
separately, by which I believe the client can optimize the
waiting process by creating threads for making request and
retrieving result separately. However, it may also make the
transaction more complicated compared with returning the
crawl result in the `Crawl` call.

### RMI
During the development of this project, the RMI framework helped
us simplify the transactions between clients and save much time
from dealing with inter-servers transactions.\
In the model we designed for distributed crawler, the client is
not going to get involved in crawling process. By using the RMI
framework, we disguise the details of our workflow by only 
providing interfaces like `singleURLCrawl` and `retrieveResult`.
On the server side, we are also using RMI framework to help the
Crawler communicate with the Database. Since we are using PAXOS
among the servers, it's very troublesome to use low-level API
to implement it. By defining corresponding interfaces, the whole
process is being simpler and more robust.
