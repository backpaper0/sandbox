#!/usr/bin/expect

spawn telnet localhost 4555
expect "Login id:"

send "root\n"
expect "Password:"

send "root\n"
expect "Welcome root. HELP for a list of commands"

send "adduser foo foo\n"
expect "User foo added"

send "adduser bar bar\n"
expect "User bar added"

send "adduser baz baz\n"
expect "User baz added"

send "quit\n"

