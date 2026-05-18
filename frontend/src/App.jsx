import { useState } from "react";
import axios from "axios";
import { FaCopy } from "react-icons/fa";

function App() {

  const [message, setMessage] = useState("");
  const [tone, setTone] = useState("Professional");
  const [replies, setReplies] = useState([]);
  const [loading, setLoading] = useState(false);
const [copiedIndex, setCopiedIndex] = useState(null);
  const tones = [
    "Professional",
    "Friendly",
    "Funny",
    "Cold",
    "Confident"
  ];

  const generateReplies = async () => {

    if (!message.trim()) return;

    try {

      setLoading(true);

      const response = await axios.post(
        "http://localhost:8080/api/reply/generate",
        {
          message,
          tone
        }
      );

      setReplies(response.data.replies);

    } catch (error) {
      console.log(error);
      alert("Something went wrong");
    }

    setLoading(false);
  };

  const copyReply = (text, index) => {

    navigator.clipboard.writeText(text);

    setCopiedIndex(index);

    setTimeout(() => {
      setCopiedIndex(null);
    }, 1500);
  };

  return (

    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-black text-white px-6 py-10">

      <div className="max-w-3xl mx-auto">

       <span className="bg-gradient-to-r from-blue-400 to-cyan-300 bg-clip-text text-transparent">
         GhostReply 👻
       </span>

        <p className="text-center text-slate-400 mb-10">
          AI-powered smart reply generator
        </p>

        {/* INPUT BOX */}

        <div className="bg-white/10 backdrop-blur-lg border border-white/10 p-6 rounded-2xl shadow-lg">

          <textarea
            rows="6"
            placeholder="Paste your conversation here..."
            className="w-full bg-slate-800 p-4 rounded-xl outline-none resize-none text-white"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />

          {/* TONES */}

          <div className="flex flex-wrap gap-3 mt-5">

            {tones.map((t) => (

              <button
                key={t}
                onClick={() => setTone(t)}
                className={`px-4 py-2 rounded-full transition-all
                  ${tone === t
                    ? "bg-blue-600"
                    : "bg-slate-700 hover:bg-slate-600"
                  }`}
              >
                {t}
              </button>

            ))}

          </div>

          {/* GENERATE BUTTON */}

          <button
            onClick={generateReplies}
            className="w-full mt-6 bg-blue-600 hover:bg-blue-700 py-3 rounded-xl font-semibold transition-all"
          >

            {loading ? (
              <div className="flex items-center justify-center gap-2">
                <div className="w-2 h-2 bg-white rounded-full animate-bounce"></div>
                <div className="w-2 h-2 bg-white rounded-full animate-bounce [animation-delay:0.2s]"></div>
                <div className="w-2 h-2 bg-white rounded-full animate-bounce [animation-delay:0.4s]"></div>
              </div>
            ) : (
              "Generate Replies"
            )}
          </button>
<button
  onClick={() => {
    setMessage("");
    setReplies([]);
  }}
  className="w-full mt-3 bg-slate-700 hover:bg-slate-600 py-3 rounded-xl transition-all"
>
  Clear
</button>
        </div>

        {/* REPLIES */}

       {/* REPLIES */}

       <div className="mt-10 space-y-4">

         {replies.length === 0 && !loading && (

           <div className="text-center text-slate-500 mt-16">
             <p className="text-2xl mb-2">👻</p>
             <p>Your AI replies will appear here</p>
           </div>

         )}

         {replies.map((reply, index) => (

           <div
             key={index}
             className="bg-white/10 backdrop-blur-lg border border-white/10 p-5 rounded-2xl flex justify-between items-center"
           >

             <p className="text-slate-200">
               {reply}
             </p>

             <button
               onClick={() => copyReply(reply, index)}
               className="text-slate-400 hover:text-white"
             >
               {
                 copiedIndex === index
                   ? "Copied!"
                   : <FaCopy />
               }
             </button>

           </div>

         ))}

         <p className="text-center text-slate-500 mt-10">
           Built with React + Spring Boot + OpenRouter
         </p>

       </div>

      </div>

    </div>

  );
}

export default App;