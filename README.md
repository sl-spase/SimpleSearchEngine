# SimpleSearchEngine
Simple version of serarch engine which processes some data and searches it for a word or a phrase using **Inverted Index** to optimize 
programe.

There are three serching strategy:
ALL, ANY and NONE
- If the strategy is ALL, the program should print lines containing all words from the query:</br>
Query:</br>
Harrington Erick</br>
Result:</br>
Erick Harrington harrington<span></span>@gmail.com

- If the strategy is ANY, the program should print lines containing at least one word from the query:<br>
Query:<br>
Erick Dwight webb<span></span>@gmail.com <br>
Result:<br>
Erick Harrington harrington<span></span>@gmail.com<br>
Erick Burgess<br>
Dwight Joseph djo<span></span>@gmail.com<br>
Rene Webb webb<span></span>@gmail.com

- If the strategy is NONE, the program should print lines that do not contain words from the query at all:<br>
Query:
djo<span></span>@gmail.com ERICK<br>
Result:<br>
Katie Jacobs<br>
Myrtle Medina<br>
Rene Webb webb<span></span>@gmail.com

For choosing searching strategy used **Strategy pattern**
