require "sinatra"

get "/" do
  content_type :json
  json = { :message=> "Hello, world!" }
  json.to_json
end

