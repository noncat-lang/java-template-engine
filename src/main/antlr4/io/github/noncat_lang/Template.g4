grammar Template;

template: element*;

element: TEXT | arg;
arg: ID;

ID: '${' [a-zA-Z0-9]+ '}';
TEXT: .+?;
