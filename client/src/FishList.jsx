import React, { useEffect, useState } from 'react';

const colorMap = {
    RED: 'red',
    GREEN: 'green',
    BLUE: 'blue',
    YELLOW: 'yellow',
    ORANGE: 'orange',
    BROWN: 'brown',
    WHITE: 'white',
    BLACK: 'black',
    CYAN: 'cyan',
    GOLD: 'gold',
};

function ColorDot({ c }) {
    const css = colorMap[(c || '').toUpperCase()] || 'gray';
    return (
        <span
            className="inline-block w-4 h-4 rounded-full mr-1 align-middle"
            style={{ backgroundColor: css }}
            title={c}
        />
    );
}

export default function FishList() {
    // fish[0] = AquariumFish, fish[1] = ClownFish, fish[2] = GoldFish
    const [fish, setFish] = useState([[], [], []]);

    useEffect(() => {
        fetch('/api/fish')
            .then((res) => res.json())
            .then((data) => setFish(data))
            .catch((err) => console.error('Error fetching fish:', err));
    }, []);

    const renderTable = (title, emoji, fish) => (
        <div className="mb-6">
            <h2 className="text-xl font-bold mb-2">
                {emoji} {title}
            </h2>
            <p className="mb-2 text-gray-700">
                Total {title}: <span className="font-semibold">{fish ? fish.length : 0}</span>
            </p>
            {fish && fish.length > 0 ? (
                <table className="table-auto w-full border">
                    <thead>
                    <tr>
                        <th className="border px-2 py-1">Age</th>
                        <th className="border px-2 py-1">Length (cm)</th>
                        <th className="border px-2 py-1">Pattern</th>
                        <th className="border px-2 py-1">Colors</th>
                    </tr>
                    </thead>
                    <tbody>
                    {fish.map((f, i) => (
                        <tr key={i}>
                            <td className="border px-2 py-1">{f.age}</td>
                            <td className="border px-2 py-1">{f.length}</td>
                            <td className="border px-2 py-1">
                                {String(f.pattern || '').toUpperCase()}
                            </td>
                            <td className="border px-2 py-1">
                                {Array.isArray(f.colors) && f.colors.length > 0 ? (
                                    f.colors.map((c, idx) => (
                                        <span key={idx} className="inline-flex items-center mr-2">
                                                <ColorDot c={c} />
                                                <span className="text-sm capitalize">{c}</span>
                                            </span>
                                    ))
                                ) : (
                                    <span className="text-gray-500">—</span>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p className="text-gray-500">No {title.toLowerCase()} yet.</p>
            )}
        </div>
    );

    return (
        <div>
            {renderTable('Aquarium Fish', '🐠', fish['AquariumFish'])}
            {renderTable('ClownFish', '🎭', fish['ClownFish'])}
            {renderTable('GoldFish', '✨', fish['GoldFish'])}
        </div>
    );
}
