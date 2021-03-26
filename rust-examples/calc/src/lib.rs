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
}

