package main

import (
	"os/exec"
	"testing"
)

func TestMain(t *testing.T) {
	cmd := exec.Command("./process")
	b, err := cmd.CombinedOutput()
	if err != nil {
		t.Error(err)
	}
	expected := "Hello World"
	actual := string(b)
	if actual != expected {
		t.Errorf(`Expected is "%v" but actual is "%v"`, expected, actual)
	}
}

func TestMainError(t *testing.T) {
	cmd := exec.Command("./process", "-err")
	b, err := cmd.CombinedOutput()
	if err == nil {
		t.Error("It terminated normally")
	}
	expected := "Error!!!"
	actual := string(b)
	if actual != expected {
		t.Errorf(`Expected is "%v" but actual is "%v"`, expected, actual)
	}
}
