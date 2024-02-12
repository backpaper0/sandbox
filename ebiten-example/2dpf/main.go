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
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 16, color.Gray{0x90}, false)
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 1, color.Black, false)

	g.player.Draw(g, screen)
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := &Game{
		ScreenWidth: 320, ScreenHeight: 120,
		player: NewPlayer(),
	}
	ebiten.SetWindowSize(g.ScreenWidth*4, g.ScreenHeight*4)
	ebiten.SetWindowTitle("2Dプラットフォーマー")
	ebiten.SetTPS(30)
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
