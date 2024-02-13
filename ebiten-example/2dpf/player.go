package main

import (
	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/inpututil"
)

type Player struct {
	x, y      int
	motion    int //0：停止、1：歩き
	dir       int //-1：左、1：右
	animation int
	jump      int //0：地面、1：上昇中、2：下降中
	vx, vy    int
}

func NewPlayer() *Player {
	return &Player{
		x: 128, y: 480 - 128, dir: 1,
	}
}

func (p *Player) Update(g *Game) {
	if ebiten.IsKeyPressed(ebiten.KeyLeft) {
		p.motion = 1
		p.dir = -1
		p.animation++
		if p.vx > -32 {
			p.vx -= 4
		}
	} else if ebiten.IsKeyPressed(ebiten.KeyRight) {
		p.motion = 1
		p.dir = 1
		p.animation++
		if p.vx < 32 {
			p.vx += 4
		}
	} else {
		p.motion = 0
		p.animation = 0
		p.vx = 0
	}
	p.x += p.vx
	if p.x < 0 {
		p.x = 0
		p.animation = 0
	}
	if p.x > g.ScreenWidth-64 {
		p.x = g.ScreenWidth - 64
		p.animation = 0
	}

	switch p.jump {
	case 0:
		if inpututil.IsKeyJustPressed(ebiten.KeySpace) {
			p.jump = 1
			p.vy = -64
		}
	case 1:
		p.vy += 8 //重力の影響
		if p.vy > 0 {
			p.jump = 2
		}
	case 2:
		p.vy += 4 //加速度
	}
	p.y += p.vy
	if p.y >= g.ScreenHeight-128 {
		p.y = g.ScreenHeight - 128
		p.vy = 0
		p.jump = 0
	}
}

func (p *Player) Draw(g *Game, screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	op.GeoM.Scale(4, 4)
	idx := 0
	if p.motion == 1 {
		idx = p.animation / 2 % 2
	}
	if p.jump > 0 {
		idx = 2
	}
	if p.dir == -1 {
		op.GeoM.Scale(-1, 1)     //左右反転
		op.GeoM.Translate(64, 0) //反転してズレた分の補正
	}
	op.GeoM.Translate(float64(p.x), float64(p.y))
	screen.DrawImage(assets.RoboImgs[idx], op)
}
