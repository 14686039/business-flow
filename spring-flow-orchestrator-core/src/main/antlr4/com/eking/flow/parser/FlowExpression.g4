// Flow.g4
grammar FlowExpression;

/*
 * =============================================================================
 * Parser Rules (Syntax)
 * =============================================================================
 */

// 顶层规则：一个程序可以包含多条语句
program: statement+ EOF;

// 语句可以是“变量赋值”或独立的“流程”
statement: (ID EQ flow) #AssignmentStatement
         | flow         #FlowStatement
         ;

// 流程：由一个或多个'conditional'单元通过'->'连接而成，这是最低优先级的操作
flow: conditional (ARROW conditional)*;

// 条件：一个'parallel'单元，后面可能跟着一个三元条件表达式
// '?' 和 ':' 的优先级高于 '->'
conditional: parallel (QMARK conditional COLON conditional)?;

// 并行：一个'atom'单元，或由括号包裹的、逗号分隔的多个流程
// '()' 和 ',' 的优先级高于 '?' 和 ':'
parallel: atom
        | LPAREN flow (COMMA flow)* RPAREN
        ;

// 原子：最基本的单元，可以是一个组件ID，也可以是括号包裹的流程（用于提升优先级）
atom: ID
    | LPAREN flow RPAREN
    ;


/*
 * =============================================================================
 * Lexer Rules (Tokens)
 * =============================================================================
 */

// Tokens
ARROW: '->';
QMARK: '?';
COLON: ':';
COMMA: ',';
EQ   : '=';
LPAREN: '(';
RPAREN: ')';

// 组件/变量名：以字母或下划线开头，后跟字母、数字或下划线
ID: [a-zA-Z_] [a-zA-Z_0-9]*;

// 忽略空白字符（空格、制表符、换行符）
WS: [ \t\r\n]+ -> skip;