import * as SpeechSDK from "microsoft-cognitiveservices-speech-sdk";
import { useEffect, useState } from 'react';

function App() {
  const [speechText, setSpeechText] = useState("")
  const [isRecording, setIsRecording] = useState(false)
  const [subscriptionKey, setSubscriptionKey] = useState("");

  const disabled = isRecording || (!subscriptionKey);

  useEffect(() => {
    fetch("/api/speech-subscription-key")
      .then(resp => resp.json())
      .then(jsonData => setSubscriptionKey(jsonData.speechSubscriptionKey));
  }, []);

  const fn = async () => {
    setIsRecording(true);
    setSpeechText("");
    const speechConfig = SpeechSDK.SpeechConfig.fromSubscription(subscriptionKey, "japanwest")
    speechConfig.speechRecognitionLanguage = "ja-JP";

    const audioConfig = SpeechSDK.AudioConfig.fromDefaultMicrophoneInput();
    const recognizer = new SpeechSDK.SpeechRecognizer(speechConfig, audioConfig);

    recognizer.recognizeOnceAsync(result => {
      setIsRecording(false);
      setSpeechText(result.text);
      recognizer.close();
    }, err => {
      setIsRecording(false);
      setSpeechText(err.toString());
      recognizer.close();
    });
  }

  return (
    <>
      <h1>Speech example</h1>
      <div>
        <button onClick={() => fn()} disabled={disabled}>
          Start
        </button>
        <p>
          {speechText}
        </p>
      </div>
    </>
  )
}

export default App
