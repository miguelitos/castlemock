/*
 Copyright 2020 Karl Dahlgren

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import React, {PureComponent} from 'react'
import axios from "axios";
import {Redirect} from "react-router-dom";
import {connect} from "react-redux";
import {setAuthenticationState} from "../redux/Actions";

class LogoutContainer extends PureComponent {

    constructor(props) {
        super(props);
        this.logout = this.logout.bind(this);

        this.logout();
    }

    logout() {
        axios
            .get("/api/rest/core/logout/")
            .then(response => {
                this.props.setAuthenticationState(false);
            })
            .catch(error => {
            });
    }

    render() {
        return <Redirect to = {{ pathname: "/web/login" }} />
    }
}

export default connect(
    null,
    { setAuthenticationState }
)(LogoutContainer);