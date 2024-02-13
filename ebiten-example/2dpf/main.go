package main

import (
	"image/color"
	"log"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/vector"
)

type Game struct {
	ScreenWidth, ScreenHeight int
	player                    *Player
}

func (g *Game) Update() error {
	g.player.Update(g)
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	vector.DrawFilledRect(screen, 0, 0, float32(g.ScreenWidth), float32(g.ScreenHeight), color.White, false)
	vector.DrawFilledRect(screen, 0, float32(g.ScreenHeight-64), float32(g.ScreenWidth), 64, color.Gray{0x90}, false)
	vector.DrawFilledRect(screen, 0, float32(g.ScreenHeight-64), float32(g.ScreenWidth), 4, color.Black, false)

	g.player.Draw(g, screen)
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := &Game{
		ScreenWidth: 1280, ScreenHeight: 480,
		player: NewPlayer(),
	}
	ebiten.SetWindowSize(g.ScreenWidth, g.ScreenHeight)
	ebiten.SetWindowTitle("2Dプラットフォーマー")
	ebiten.SetTPS(30)
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
