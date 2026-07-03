import React, { useState } from 'react';
import ZooOverview from './ZooOverview';
import PenguinList from './PenguinList';
import PredatorsList from './PredatorsList';
import FishCList from './FishList';
import AddAnimalForm from "./AddAnimalForm";
import FeedAll from "./FeedAll";
import SoundAll from "./SoundAll";
import DominantColors from "./DominantColors";
import ExitZoo from "./ExitZoo";
import IncreasingAgeOneYear from "./IncreasingAgeOneYear";
import LoadZooData from "./LoadZooData";
import VeterinaryClinicList from "./VeterinaryClinicList";

const animalTypes = ['Penguin', 'Lion', 'AquariumFish'];

export default function ZooApp() {
    const [selectedAction, setSelectedAction] = useState(null);
    const [selectedAnimalType, setSelectedAnimalType] = useState('Penguin');

    const getButtonClassName = (actionName) => {
        const baseClassName = "bg-gray-200 hover:bg-gray-300 p-2 rounded text-left";
        const selectedClassName = "bg-blue-500 text-white hover:bg-blue-600 p-2 rounded  text-left";
        return selectedAction === actionName ? ` ${selectedClassName}` : baseClassName;
    };
    const renderActionContent = () => {
        switch (selectedAction) {
            case 'loadZooData':
                return <LoadZooData />;
            case 'showZoo':
                return <ZooOverview />;
            case 'showPredators':
                return <PredatorsList />;
            case 'showPenguins':
                return <PenguinList />;
            case 'showFish':
                return <FishCList />;
            case 'addAnimal':
                return  <AddAnimalForm />;
            case 'feedAll':
                return <FeedAll />;
            case 'soundAll':
                return <SoundAll />;
            case 'dominantColors':
                return <DominantColors />;
            case 'increasingAgeOneYear':
                return <IncreasingAgeOneYear />;
            case 'veterinaryClinicList':
                return <VeterinaryClinicList />;
            case 'exit':
                return <ExitZoo />;
            default:
                return <div className="text-gray-500">Please select an action from the menu.</div>;
        }
    };

    return (
        <div className="flex flex-col md:flex-row p-4 gap-4">
            {/* Side menu */}
            <div className="flex flex-col gap-2 w-full md:w-1/4">
                <button onClick={() => setSelectedAction('loadZooData')} className={getButtonClassName('loadZooData')}>
                    Load Zoo Data
                </button>
                <button onClick={() => setSelectedAction('showZoo')} className={getButtonClassName('showZoo')}>
                    Show zoo details
                </button>
                <button onClick={() => setSelectedAction('showPredators')} className={getButtonClassName('showPredators')}>
                    Show Predators
                </button>
                <button onClick={() => setSelectedAction('showPenguins')} className={getButtonClassName('showPenguins')}>
                    Show penguins
                </button>
                <button onClick={() => setSelectedAction('showFish')} className={getButtonClassName('showFish')}>
                    Show fish
                </button>
                <button onClick={() => setSelectedAction('addAnimal')} className={getButtonClassName('addAnimal')}>
                    Add animal
                </button>
                <button onClick={() => setSelectedAction('feedAll')} className={getButtonClassName('feedAll')}>
                    Feed all animals
                </button>
                <button onClick={() => setSelectedAction('soundAll')} className={getButtonClassName('soundAll')}>
                    Show sound all animals
                </button>
                <button onClick={() => setSelectedAction('dominantColors')} className={getButtonClassName('dominantColors')}>
                    Show dominant colors
                </button>
                <button onClick={() => setSelectedAction('increasingAgeOneYear')} className={getButtonClassName('increasingAgeOneYear')}>
                    Show increasing all animal age by One year
                </button>
                <button onClick={() => setSelectedAction('veterinaryClinicList')} className={getButtonClassName('veterinaryClinicList')}>
                    Show Veterinary Clinic
                </button>
                <button onClick={() => setSelectedAction('exit')} className="bg-red-200 hover:bg-red-300 p-2 rounded text-left">
                    Exit
                </button>
            </div>

            {/* Dynamic content area */}
            <div className="flex-1 border rounded p-4 bg-white shadow">
                {renderActionContent()}
            </div>
        </div>
    );
}
