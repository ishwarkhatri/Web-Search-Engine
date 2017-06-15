# Web Search Engine

The project imitates working of a web search engine that involves 3 stages of execution.
In the first stage, it crawls through all the web pages provided to it and tokenizes words after stemming and filtering out unnecessary details such as stop words, special characters, symbols, images etc. 

In second stage, it works upon the tokens that were created in stage one. Using these tokens it creates a dictionary and the posting file that maintains mapping of each word with the names of documents in which it has appeared.

In third and final stage, the user entered statement is analyzed and searched upon the dictionary and posting file. After retrieving results from dictionary and posting file, it ranks each document based on the number of times and number of words in the statement that occurred in the document. The most exact matching document is ranked prior to the less matching documents. Final result shows the list of documents as per rankings and also the summary of statements after each document which match user entered statement.
