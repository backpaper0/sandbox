package main

import (
    "context"
    "fmt"
    "os"

    "dagger.io/dagger"
)

func main() {
    if err := build(context.Background()); err != nil {
        fmt.Println(err)
    }
}

func build(ctx context.Context) error {
    fmt.Println("Building with Dagger")

    // initialize Dagger client
    client, err := dagger.Connect(ctx, dagger.WithLogOutput(os.Stdout))
    if err != nil {
        return err
    }
    defer client.Close()

    // get reference to the local project
    src := client.Host().Workdir()

	cacheKey := "m2-cache-v1"

	cache := client.CacheVolume(cacheKey)

	container := client.Container().From("bellsoft/liberica-openjdk-debian:17")

    container = container.WithMountedDirectory("/src", src).WithWorkdir("/src")

	container = container.WithMountedCache("/m2", cache)

    // define the application build command
    path := "target/"
    container = container.Exec(dagger.ContainerExecOpts{
		Args: []string{"./mvnw", "package", "-Dmaven.repo.local=/m2/repository", "--batch-mode", "--no-transfer-progress"},
    })

    // get reference to build output directory in container
    output := container.Directory(path)

    // write contents of container build/ directory to the host
    _, err = output.Export(ctx, path)
    if err != nil {
        return err
    }

    return nil
}
