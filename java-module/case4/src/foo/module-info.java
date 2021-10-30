module mod.foo {
    requires mod.bar;
    // bazはautomatic mmoduleなのでJARの名前がモジュール名になる
    requires baz;
}
