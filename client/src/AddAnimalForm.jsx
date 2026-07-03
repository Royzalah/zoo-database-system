import React, { useState } from 'react';

const animalFamilies = {
    Birds: ['Penguin'],
    Predators: ['Lion', 'Tiger'],
    Fish: ['AquariumFish', 'ClownFish', 'GoldFish']
};

export default function AddAnimalForm() {
    const [selectedFamily, setSelectedFamily] = useState('Birds');
    const [selectedAnimalType, setSelectedAnimalType] = useState('Penguin');
    const [fishMode, setFishMode] = useState("none");
    const [formData, setFormData] = useState({
        name: '',
        age: '',
        height: '',
        weight: '',
        gender: '',
        pattern: '',
        length: '',
        colors: '',
        quantity: ''
    });
    const [message, setMessage] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async () => {
        setIsSubmitting(true);
        let dataString = `${selectedAnimalType}`;

        if (selectedAnimalType === 'Penguin') {
            dataString += `,${formData.name},${formData.age},${formData.height}`;
        } else if (selectedAnimalType === 'Lion' || selectedAnimalType === 'Tiger') {
            dataString += `,${formData.name},${formData.age},${formData.weight},${formData.gender}`;
        } else if (selectedFamily === 'Fish') {
            if (fishMode === "random") {
                dataString = `RandomFish,${formData.quantity}`;
            } else if (fishMode === "single") {
                if (selectedAnimalType === 'AquariumFish') {
                    dataString += `,${formData.age},${formData.length},${formData.pattern},${formData.colors}`;
                } else if (selectedAnimalType === 'ClownFish') {
                    dataString += `,${formData.age},${formData.length},${formData.colors}`;
                } else if (selectedAnimalType === 'GoldFish') {
                    dataString += `,${formData.age},${formData.length},${formData.colors}`;
                }
            }
        }

        try {
            const res = await fetch('/api/add-animal', {
                method: 'POST',
                headers: { 'Content-Type': 'text/plain' },
                body: dataString
            });

            const text = await res.text();
            if (!res.ok || text.startsWith("Failed")) {
                setMessage(text || 'Error adding animal.');
                return;
            }
            setMessage(text || 'Added successfully.');
            setFormData({
                name: '',
                age: '',
                height: '',
                weight: '',
                gender: '',
                pattern: '',
                length: '',
                colors: '',
                quantity: ''
            });
        } catch (err) {
            console.error('Failed to add animal:', err);
            setMessage(`Error adding animal. ,${err}`);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="space-y-4">
            {/* Family selector */}
            <div>
                <label className="block">Select animal family:</label>
                <select
                    value={selectedFamily}
                    onChange={(e) => {
                        setSelectedFamily(e.target.value);
                        const firstType = animalFamilies[e.target.value][0];
                        setSelectedAnimalType(firstType);
                        setFishMode("none");
                    }}
                    className="border p-2"
                >
                    {Object.keys(animalFamilies).map((family) => (
                        <option key={family} value={family}>{family}</option>
                    ))}
                </select>
            </div>

            {/* Type selector for Birds/Predators */}
            {selectedFamily !== "Fish" && (
                <div>
                    <label className="block">Select animal type:</label>
                    <select
                        value={selectedAnimalType}
                        onChange={(e) => setSelectedAnimalType(e.target.value)}
                        className="border p-2"
                    >
                        {animalFamilies[selectedFamily].map((type) => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                </div>
            )}

            {/* Fish mode selector */}
            {selectedFamily === "Fish" && (
                <div>
                    <label className="block font-semibold">Fish input mode:</label>
                    <div className="flex gap-4">
                        <label>
                            <input
                                type="radio"
                                value="random"
                                checked={fishMode === "random"}
                                onChange={() => setFishMode("random")}
                            /> 🎲 Random
                        </label>
                        <label>
                            <input
                                type="radio"
                                value="single"
                                checked={fishMode === "single"}
                                onChange={() => setFishMode("single")}
                            /> 🐟 With values
                        </label>
                    </div>
                </div>
            )}

            {/* If fish single mode, choose fish type */}
            {selectedFamily === "Fish" && fishMode === "single" && (
                <div>
                    <label className="block">Select fish type:</label>
                    <select
                        value={selectedAnimalType}
                        onChange={(e) => setSelectedAnimalType(e.target.value)}
                        className="border p-2"
                    >
                        {animalFamilies.Fish.map((type) => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                </div>
            )}

            {/* Name field */}
            {(selectedAnimalType === 'Penguin' ||
                selectedAnimalType === 'Lion' ||
                selectedAnimalType === 'Tiger') && (
                <div>
                    <label className="block">Name:</label>
                    <input
                        type="text"
                        name="name"
                        placeholder="Name"
                        value={formData.name}
                        onChange={handleChange}
                        className="block border p-2 w-full"
                    />
                </div>

            )}
            {/* Age field */}
            {(selectedAnimalType === 'Penguin' ||
                selectedAnimalType === 'Lion' ||
                selectedAnimalType === 'Tiger' ||
                (selectedFamily === 'Fish' && fishMode === "single")) && (
                <div>
                    <label className="block">Age:</label>
                    <input
                        type="number"
                        name="age"
                        placeholder="Age"
                        value={formData.age}
                        onChange={handleChange}
                        className="block border p-2 w-full"
                    />
                </div>
            )}

            {/* Extra fields */}
            {selectedAnimalType === 'Penguin' && (
                <div>
                    <label className="block">Height (cm):</label>
                    <input
                        type="number"
                        name="height"
                        placeholder="Height"
                        value={formData.height}
                        onChange={handleChange}
                        className="block border p-2 w-full"
                    />
                </div>
            )}

            {(selectedAnimalType === 'Lion' || selectedAnimalType === 'Tiger') && (
                <>
                    <div>
                        <label className="block">Weight (kg):</label>
                        <input
                            type="number"
                            name="weight"
                            placeholder="Weight"
                            value={formData.weight}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                    <div>
                        <label className="block">Gender:</label>
                        <input
                            type="text"
                            name="gender"
                            placeholder="Gender (e.g. male, female)"
                            value={formData.gender}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                </>
            )}

            {selectedFamily === "Fish" && fishMode === "random" && (
                <div>
                    <label className="block">Quantity:</label>
                    <input
                        type="number"
                        name="quantity"
                        placeholder="Quantity of random fish"
                        value={formData.quantity}
                        onChange={handleChange}
                        className="block border p-2 w-full"
                    />
                </div>
            )}

            {fishMode === "single" && selectedAnimalType === 'AquariumFish' && (
                <>
                    <div>
                        <label className="block">Length (cm):</label>
                        <input
                            type="number"
                            name="length"
                            placeholder="Length"
                            value={formData.length}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                    <div>
                        <label className="block">Colors:(BLACK, WHITE, GREEN, ORANGE, BLUE, YELLOW, BROWN, GOLD, RED, CYAN)</label>
                        <input
                            type="text"
                            name="colors"
                            placeholder="Enter colors comma-separated"
                            value={formData.colors}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                    <div>
                        <label className="block">Pattern:(DOTS, STRIPES, SPOTS, SMOOTH)</label>
                        <input
                            type="text"
                            name="pattern"
                            placeholder="Enter one pattern"
                            value={formData.pattern}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                </>
            )}

            {fishMode === "single" && selectedAnimalType === 'ClownFish' && (
                <>
                    <div>
                        <label className="block">Length (cm):</label>
                        <input
                            type="number"
                            name="length"
                            placeholder="Length"
                            value={formData.length}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                    <div>
                        <label className="block">Colors:(BLACK, WHITE, ORANGE)</label>
                        <input
                            type="text"
                            name="colors"
                            placeholder="Enter colors comma-separated"
                            value={formData.colors}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                </>
            )}

            {fishMode === "single" && selectedAnimalType === 'GoldFish' && (
                <>
                    <div>
                        <label className="block">Length (cm):</label>
                        <input
                            type="number"
                            name="length"
                            placeholder="Length"
                            value={formData.length}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                    <div>
                        <label className="block">Color:(BLACK, ORANGE, GOLD, YELLOW)</label>
                        <input
                            type="text"
                            name="colors"
                            placeholder="Enter one color"
                            value={formData.colors}
                            onChange={handleChange}
                            className="block border p-2 w-full"
                        />
                    </div>
                </>
            )}

            <button
                onClick={handleSubmit}
                disabled={isSubmitting}
                className="bg-blue-500 text-white px-4 py-2 rounded"
            >
                {isSubmitting ? "Adding..." : "Add"}
            </button>

            {message && <p className="text-green-600 mt-2">{message}</p>}
        </div>
    );
}
