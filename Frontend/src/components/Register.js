import React from "react";
// zishan01
// 12345

export default function Register() {
    // registerStatusText is used to save the message whether the account is registered successfully or not
    const [registerStatusText, setRegisterStatusText] = React.useState("")

    // formData is used to save the user input values
    const [formData, setFormData] = React.useState(
        {
            username: "",
            password: ""
        }
    )

    // inputWarningText is used to save messages that prompts the user to change the information.
    const [inputWarningText, setInputWarningText] = React.useState({
        usernameReminder: "",
        passwordReminder: ""
    })

    /**
     * Check that the input conforms to the specification.
     * @param data
     * @returns {boolean}
     */
    function checkInput(data) {
        const regex = /[_\\-\\.0-9a-z]/
        let result = true
        for (let char of data) {
            if (char.match(regex) === null) {
                result = false
            }
        }
        return result && (data.length <= 127 && data.length >= 1)
    }


    function updateFormData(event) {
        event.preventDefault()
        console.log(event.target.value)
        setFormData(prevFormData => {
            return {
                ...prevFormData,
                [event.target.name]: event.target.value
            }
        })
    }

    function handleSubmit(event) {
        event.preventDefault()

        const nameIsValid = checkInput(formData.username)
        const passwordIsValid = checkInput(formData.password)

        setInputWarningText(prevState => ({
            usernameReminder: "",
            passwordReminder: ""
        }))

        if (nameIsValid && passwordIsValid) {

            fetch('http://localhost:8081/bankSys/user/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            })
                .then(response => response.json())
                .then(data => {
                    // setBalance(data.data.user.balance)
                    if (data.status === 200) {
                        setRegisterStatusText(`Congratulations ${formData.username}, You have registered successfully!`)
                    } else {
                        setRegisterStatusText(`This username has been used, please try another one~`)
                    }
                })
                .catch((error) => {
                    setRegisterStatusText(`Error: ${error}`)
                });
        }
        if (!nameIsValid || !passwordIsValid) {
            setInputWarningText({
                usernameReminder: nameIsValid ? "" : "Invalid account name",
                passwordReminder: passwordIsValid ? "" : "Invalid password"
            })
        }
    }

    return (
        <div className="register">
            <h1 className="register-title">REGISTER TODAY</h1>
            <h2 className="register-subtitle">Instructions</h2>
            <p className="register-instruction">Account name and password are restricted to underscores, hyphens, dots, digits, and lowercase alphabetical characters.</p>
            <p className="register-instruction">Account name and password should be between 1 and 127 in length.</p>
            <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="account name"
                        name="username"
                        onChange={updateFormData}
                        value={formData.username}
                        className="register-form-input"
                    />
                    <p className="register-form-reminder">{inputWarningText.usernameReminder}</p>


                    <input
                        type="text"
                        placeholder="password"
                        name="password"
                        onChange={updateFormData}
                        value={formData.password}
                        className="register-form-input"
                    />
                    <p className="register-form-reminder">{inputWarningText.passwordReminder}</p>

                <button className="btn register-form-btn">Register</button>
            </form>
            <p className="register-statusText">{registerStatusText}</p>
        </div>
    )
}