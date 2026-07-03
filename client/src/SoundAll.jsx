import React, { useState, useEffect, useRef } from "react";


export default function SoundAll() {
    const [soundString, setSoundString] = useState("");
    const [soundArray, setSoundArray] = useState([]);
    const [isPlaying, setIsPlaying] = useState(false);
    const stopRef = useRef(false);
    const [message, setMessage] = useState('');

    function splitAnimalSounds(str) {
        const tokens = ["ROAR", "roar", "squack", "blob"];
        const result = [];
        let i = 0;

        while (i < str.length) {
            let matched = false;
            for (const t of tokens) {
                if (str.startsWith(t, i)) {
                    result.push(t);
                    i += t.length;
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                setMessage("Unrecognized sound at position " + i);
                throw new Error("Unrecognized sound at position " + i);
            }
        }
        return result;
    }

    useEffect(() => {
        fetch("/api/soundAll")
            .then((res) => res.text())
            .then((data) => {
                setSoundString(data);
                setSoundArray(splitAnimalSounds(data));
            })
            .catch((err) => console.error("Error fetching soundAll:", err));
    }, []);

    const audioMap = {
        ROAR: "/sounds/lion.mp3",
        roar: "/sounds/tiger.mp3",
        squack: "/sounds/penguin.mp3",
        blob: "/sounds/fish.mp3"
    };

    const playAll = async () => {
        setIsPlaying(true);
        stopRef.current = false;

        for (let i = 0; i < soundArray.length; i++) {
            if (stopRef.current) break;

            const sound = soundArray[i];
            const audioUrl = audioMap[sound];
            if (audioUrl) {
                const audio = new Audio(audioUrl);
                await new Promise((resolve) => {
                    audio.onended = resolve;
                    audio.play();
                });
            } else {
                await new Promise((r) => setTimeout(r, 300));
            }
        }

        setIsPlaying(false);
    };

    const stopAll = () => {
        stopRef.current = true;
        setIsPlaying(false);
    };

    return (
        <div>
            <h2 className="text-3xl font-bold mb-4">🔊 Sounding all animals</h2>
            <div className="text-2xl text-black-500 break-all bg-gray-100 p-3 rounded">
                {soundString}
            </div>
            <button
                onClick={isPlaying ? stopAll : playAll}
                className={`mt-3 px-4 py-2 rounded shadow ${
                    isPlaying ? "bg-red-500" : "bg-green-500"
                } text-white`}
            >
                {isPlaying ? "⏹️ Stop" : "▶️ Play All Sounds"}
            </button>
            {message && <p className="text-red-600 mt-2">{message}</p>}
        </div>
    );
}
