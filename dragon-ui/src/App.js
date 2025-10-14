import './App.css';
import axios from "axios";
import {useEffect, useState} from "react";

function adminMove({part, percent}) {
    return axios.post("/dragon/admin-move", {part, percent});
}

const adminParts = ["all", "head.neck.both", "neck.lower.both", "head.jaw", "head.leftneck", "head.rightneck", "neck.lowerleft", "neck.lowerright", "rightwing.hand"]

function App() {
    const [dragon, setDragon] = useState(null);
    useEffect(() => {
        const interval = setInterval(() => {
            axios.get("/dragon", { timeout: 500 })
                .then(res => setDragon(res.data))
                .catch(err => console.log(err));
        }, 500)
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="App">
            <Button onClick={() => axios.post("/dragon/reset")}>Reset</Button>
            <Button onClick={() => axios.post("/dragon/stop")}>Stop</Button>
            <Button onClick={() => axios.post("/dragon/sleep")}>Sleep</Button>
            <Button onClick={() => axios.post("/dragon/wakeup")}>Wake Up</Button>
            <Button onClick={() => axios.post("/dragon/roar")}>Roar</Button>

            {adminParts.map((part) => <AdminMovePart key={part} part={part}/>)}

            <pre style={{textAlign: "left"}}>
                {JSON.stringify(dragon, null, 2)}
            </pre>
        </div>
    );
}

function AdminMovePart({part}) {
    return <div className="AdminMovePart">
        <Button onClick={() => adminMove({part, percent: 1})}>admin {part} open</Button>
        <Button onClick={() => adminMove({part, percent: 0})}>admin {part} close</Button>
    </div>
}

function Button({onClick, children}) {
    return <button className="Button" onClick={onClick}>{children}</button>
}

export default App;
