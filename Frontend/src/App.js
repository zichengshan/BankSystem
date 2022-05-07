import React from "react";
import './App.css';
import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import {Routes, Route, Link} from "react-router-dom"

function App() {
    // isLoginStatus === true => the user login successfully
    const [isLoginStatus, SetIsLoginStatus] = React.useState(true)
    const [userInfo, setUserInfo] = React.useState(
        {
            username: "",
            id: "",
            balance: "",
            token: ""
        }
    )

    /**
     * updateUserInfo() receives the object from Login component to update the user info
     * @param userInfo
     */
    function updateUserInfo(userInfo) {
        setUserInfo(prevInfo => ({
            username: userInfo.data.user.username,
            id: userInfo.data.user.id,
            balance: userInfo.data.user.balance,
            token: userInfo.data.token
        }))
        SwitchLoginStatus()
    }

    /**
     * SwitchLoginStatus is used to switch the login status from true to false, or from false to true
     * @constructor
     */
    function SwitchLoginStatus() {
        SetIsLoginStatus(prevStatus => !prevStatus)
    }

    return (
        <div>
            <nav>
                <Link to="/">Register </Link>
                <Link to="/about">{isLoginStatus? "Login" : "Home"}</Link>
            </nav>

            <Routes>
                <Route exact path="/" element={<Register/>}/>
                <Route exact path="/about"
                       element={
                           isLoginStatus ?
                               <Login updateUserInfo={updateUserInfo}/>
                               : <Home
                                   username={userInfo.username}
                                   id={userInfo.id}
                                   balance={userInfo.balance}
                                   token={userInfo.token}
                                   changeLoginStatus={SwitchLoginStatus}
                               />}
                />
            </Routes>
        </div>
    )
}

export default App;

