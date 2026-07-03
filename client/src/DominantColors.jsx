import React, { useState, useEffect } from 'react';
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


export default function DominantColors() {
    const [dominantColorsRes, setDominantColorsRes] = useState('');

    useEffect(() => {
        fetch('/api/dominantColors')
            .then((res) => {
                return res.text();
            })
            .then((data) => {
                setDominantColorsRes(data.split(','));
            })
            .catch((err) => console.error('Error fetching dominantColorsRes:', err));
    }, []);

    return (
        <div>
            <h2 className="text-3xl font-bold mb-4">🎨 Dominant Colors</h2>
            <p className="border px-2 py-1">
                {Array.isArray(dominantColorsRes) && dominantColorsRes.length > 0 ? (
                    dominantColorsRes.map((c, idx) => (
                        <span key={idx} className="inline-flex items-center mr-2">
                                                <ColorDot c={c} />
                                                <span className="text-sm capitalize">{c}</span>
                                            </span>
                    ))
                ) : (
                    <span className="text-gray-500">—</span>
                )}
            </p>

        </div>
    );
}