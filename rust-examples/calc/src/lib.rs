use std::str::Chars;

struct Lexer<'a> {
    source: Chars<'a>,
    c: Option<char>,
}

#[derive(Debug, PartialEq)]
enum TokenType {
    Num,
    Add,
    Eof,
}

#[derive(Debug, PartialEq)]
struct Token(TokenType, String);

impl<'a> Lexer<'a> {

    fn new(source: &'a String) -> Lexer<'a> {
        let mut source = source.chars();
        let c = None;
        let mut lexer = Lexer {
            source,
            c,
        };
        lexer.consume();
        lexer
    }

    fn consume(&mut self) {
        self.c = self.source.next();
    }

    fn next(&mut self) -> Token {
        while let Some(c) = self.c {
            match c {
                ' ' => {
                    self.consume();
                },
                '0'..='9' => {
                    let mut s = String::new();
                    s.push(c);
                    self.consume();
                    while let Some(c) = self.c {
                        match c {
                            '0'..='9' => {
                                s.push(c);
                                self.consume();
                            },
                            _ => {
                                break;
                            },
                        }
                    }
                    return Token(TokenType::Num, s)
                },
                '+' => {
                    self.consume();
                    return Token(TokenType::Add, String::from("+"))
                },
                _ => {
                    panic!("Illegal character: {}", c);
                }
            }
        }
        Token(TokenType::Eof, String::new())
    }
}

#[derive(Debug, PartialEq)]
enum Node {
    Num(i32),
    Add(Box<Node>, Box<Node>),
}

struct Parser<'a> {
    lexer: Lexer<'a>,
    token: Token,
}

impl<'a> Parser<'a> {
    
    fn new(source: &'a String) -> Parser<'a> {
        let mut lexer = Lexer::new(source);
        let token = lexer.next();
        Parser {
            lexer,
            token,
        }
    }

    fn consume(&mut self) {
        self.token = self.lexer.next();
    }

    fn expect(&mut self, tokenType: TokenType) {
        if self.token.0 != tokenType {
            panic!("Unexpected token: expected = {:?}, actual = {:?}", tokenType, self.token);
        }
    }

    fn parse(&mut self) -> Node {
        let node = self.parse_add();
        self.expect(TokenType::Eof);
        node
    } 

    fn parse_add(&mut self) -> Node {
        let mut node = self.parse_num();
        while self.token.0 == TokenType::Add {
            self.consume();
            let right = self.parse_num();
            node = Node::Add(Box::new(node), Box::new(right))
        }
        node
    }

    fn parse_num(&mut self) -> Node {
        self.expect(TokenType::Num);
        let node = Node::Num(self.token.1.parse().unwrap());
        self.consume();
        node
    }
}

fn evaluate(node: Node) -> i32 {
    match node {
        Node::Num(value) => value,
        Node::Add(left, right) => evaluate(*left) + evaluate(*right),
    }
}

#[cfg(test)]
mod tests {

    use super::*;

    #[test]
    fn next_eof() {
        let source = String::new();
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Eof, String::new()), token);
    }

    #[test]
    fn next_num_0() {
        let source = String::from("0");
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Num, String::from("0")), token);
    }

    #[test]
    fn next_num_12() {
        let source = String::from("12");
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Num, String::from("12")), token);
    }

    #[test]
    fn next_num_345() {
        let source = String::from("345");
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Num, String::from("345")), token);
    }

    #[test]
    fn next_num_6789() {
        let source = String::from("6789");
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Num, String::from("6789")), token);
    }

    #[test]
    fn next_add() {
        let source = String::from("+");
        let mut sut = Lexer::new(&source);
        let token = sut.next();
        assert_eq!(Token(TokenType::Add, String::from("+")), token);
    }

    #[test]
    #[should_panic]
    fn next_illegal_char() {
        let source = String::from("abc");
        let mut sut = Lexer::new(&source);
        sut.next();
    }

    #[test]
    fn next_combination() {
        let source = String::from("1234 + 567890");
        let mut sut = Lexer::new(&source);
        assert_eq!(Token(TokenType::Num, String::from("1234")), sut.next());
        assert_eq!(Token(TokenType::Add, String::from("+")), sut.next());
        assert_eq!(Token(TokenType::Num, String::from("567890")), sut.next());
        assert_eq!(Token(TokenType::Eof, String::from("")), sut.next());
    }

    #[test]
    fn parse_num() {
        let source = String::from("12345");
        let mut sut = Parser::new(&source);
        assert_eq!(Node::Num(12345), sut.parse());
    }

    #[test]
    fn parse_num_add_num() {
        let source = String::from("678 + 90");
        let mut sut = Parser::new(&source);
        assert_eq!(Node::Add(Box::new(Node::Num(678)), Box::new(Node::Num(90))), sut.parse());
    }

    #[test]
    fn parse_num_add_num_repeat() {
        let source = String::from("1 + 23 + 456 + 7890");
        let mut sut = Parser::new(&source);
        let a = Box::new(Node::Num(1));
        let b = Box::new(Node::Num(23));
        let c = Box::new(Node::Num(456));
        let d = Box::new(Node::Num(7890));
        let e = Box::new(Node::Add(a, b));
        let f = Box::new(Node::Add(e, c));
        let g = Node::Add(f, d);
        assert_eq!(g, sut.parse());
    }

    #[test]
    fn evaluate_num() {
        let source = String::from("12345");
        let node = Parser::new(&source).parse();
        assert_eq!(12345, evaluate(node));
    }

    #[test]
    fn evaluate_num_add_num() {
        let source = String::from("678 + 90");
        let node = Parser::new(&source).parse();
        assert_eq!(678 + 90, evaluate(node));
    }

    #[test]
    fn evaluate_num_add_num_repeat() {
        let source = String::from("1 + 23 + 456 + 7890");
        let node = Parser::new(&source).parse();
        assert_eq!(1 + 23 + 456 + 7890, evaluate(node));
    }
}

