from locust import HttpUser, task

class Demo(HttpUser):
    @task
    def demo(self):
        self.client.get("/demo", params={ "id": 1 })
