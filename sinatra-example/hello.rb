require "sinatra"

get "/" do
  content_type :json
  json = { :message=> "Hello, world!" }
  json.to_json
end

post "/" do
  json = JSON.parse(request.body.string)
  content_type :json
  json.to_json
end

