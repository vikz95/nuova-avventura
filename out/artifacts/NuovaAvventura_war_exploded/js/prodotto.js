const categoria = document.getElementById('categoria');
categoria.addEventListener('change', mostraDettagli);

function mostraDettagli() {
    switch (categoria.value) {
        case 'LIBRI':
            html = `<div class="row justify-content-center">
                    <div class="col-md-5">
                        <h3>Dettagli Libro</h3>
                        <div class="form-group">
                            <label for="autori">Autori</label>
                            <input type="text" class="form-control" name="autori" id="autori" maxlength="255" required>
                            <div class="invalid-feedback">Inserisci i nomi degli autori separati da virgola (max 255 caratteri)</div>
                        </div>
                        <div class="form-group">
                            <label for="editore">Editore</label>
                            <input type="text" class="form-control" name="editore" id="editore" maxlength="45" required>
                            <div class="invalid-feedback">Inserisci l'editore (max 45 caratteri)</div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-6">
                                <div class="form-check-label mb-2">È un fumetto?</div>
                                <label class="mr-5">
                                    <input type="radio" class="form-check form-check-inline" name="fumetto" value="si">sì
                                </label>
                                <label>
                                    <input type="radio" class="form-check form-check-inline" name="fumetto" value="no"
                                           checked>no
                                </label>
                            </div>
                            <div class="form-group col-6">
                                <label for="pagine">Numero Pagine</label>
                                <input type="number" class="form-control" name="pagine" id="pagine" min="1" required>
                                <div class="invalid-feedback">Inserisci il numero di pagine del libro</div>
                            </div>
                        </div>
                    </div>
                </div>`;
            break;
        case 'ALBUM':
            html = `<div class="row justify-content-center">
                    <div class="col-md-5">
                        <h3>Dettagli Album</h3>
                        <div class="form-group">
                            <label for="autori">Autori</label>
                            <input type="text" class="form-control" name="autori" id="autori" maxlength="255" required>
                            <div class="invalid-feedback">Inserisci gli autori separati da virgola (max 255 caratteri)</div>
                        </div>
                        <div class="form-group">
                            <label for="etichetta">Etichetta</label>
                            <input type="text" class="form-control" name="etichetta" id="etichetta" maxlength="45" required>
                            <div class="invalid-feedback">Inserisci l'etichetta discografica</div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-6">
                                <div class="form-check-label mb-2">Tipo di supporto</div>
                                <label class="mr-5">
                                    <input type="radio" class="form-check form-check-inline" name="supporto" value="CD"
                                           checked>CD
                                </label>
                                <label>
                                    <input type="radio" class="form-check form-check-inline" name="supporto"
                                           value="VINILE">Vinile
                                </label>
                            </div>
                            <div class="form-group col-6">
                                <label for="numero-supporti">Numero Supporti</label>
                                <input type="number" class="form-control" name="numeroSupporti" id="numero-supporti" min="1" max="127" required>
                                <div class="invalid-feedback">Inserisci il numero di dischi all'interno della confezione</div>
                            </div>
                        </div>
                        <h5 class="mb-4">Aggiungi le canzoni all'album</h5>
                        <div id="canzoni">
                            <div class="form-row">
                                <div class="form-group col-8">
                                    <label for="canzone-1">Canzone 1</label>
                                    <input type="text" class="form-control" name="canzone-1" id="canzone-1" maxlength="45" required>
                                    <div class="invalid-feedback">Inserisci il nome della canzone (max 45 caratteri)</div>
                                </div>
                                <div class="form-group col-4">
                                    <label for="durata-1">Durata</label>
                                    <input type="time" step="1" class="form-control" name="durata-1" id="durata-1" required>
                                    <div class="invalid-feedback">Inserisci la durata della canzone</div>
                                </div>
                            </div>
                        </div>
                        <div class="d-flex justify-content-center">
                            <button type="button" class="btn btn-danger rounded-circle mb-3 mr-3" id="rimuovi-canzone" onclick="rimuoviCanzone()">&ndash;</button>
                            <button type="button" class="btn btn-success rounded-circle mb-3" id="aggiungi-canzone" onclick="aggiungiCanzone()">+</button>
                        </div>
                    </div>
                </div>`;
            break;
        case 'FILM':
            html = `<div class="row justify-content-center">
                    <div class="col-md-5">
                        <h3>Dettagli Film</h3>
                        <div class="form-group">
                            <label for="regia">Regia</label>
                            <input type="text" class="form-control" name="regia" id="regia" maxlength="45" required>
                            <div class="invalid-feedback">Inserisci il nome del regista (max 45 caratteri)</div>
                        </div>
                        <div class="form-group">
                            <label for="attori">Attori</label>
                            <input type="text" class="form-control" name="attori" id="attori" maxlength="255" required>
                            <div class="invalid-feedback">Inserisci da 3 a 6 attori separati da virola (max 255 caratteri)</div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-6">
                                <div class="form-check-label mb-2">Tipo di supporto</div>
                                <label class="mr-5">
                                    <input type="radio" class="form-check form-check-inline" name="supporto" value="DVD"
                                           checked>DVD
                                </label>
                                <label>
                                    <input type="radio" class="form-check form-check-inline" name="supporto"
                                           value="BLURAY">Blu-ray
                                </label>
                            </div>
                            <div class="form-group col-6">
                                <label for="paese">Paese</label>
                                <input type="text" class="form-control" name="paese" id="paese" maxlength="45" required>
                                <div class="invalid-feedback">Inserisci il paese di produzione (max 45 caratteri)</div>
                            </div>
                        </div>
                    </div>
                </div>`;
            break;
        case 'VIDEOGIOCHI':
            html = `<div class="row justify-content-center">
                    <div class="col-md-5">
                        <h3>Dettagli Videogioco</h3>
                        <div class="form-group">
                            <label for="produttore">Produttore</label>
                            <input type="text" class="form-control" name="produttore" id="produttore" maxlength="45" required>
                            <div class="invalid-feedback">Inserisci l'azienda sviluppatrice del gioco</div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-6">
                                <label for="piattaforma">Piattaforma</label>
                                <input type="text" class="form-control" name="piattaforma" id="piattaforma" list="piattaforme" maxlength="45" required>
                                <datalist id="piattaforme">
                                    <option value="Xbox One"></option>
                                    <option value="PS4"></option>
                                    <option value="Nintendo Switch"></option>
                                    <option value="Nintendo 3DS"></option>
                                    <option value="PC Windows"></option>
                                    <option value="MacOS"></option>
                                </datalist>
                                <div class="invalid-feedback">Inserisci la piattaforma su cui il gioco può essere riprodotto</div>
                            </div>
                            <div class="form-group col-6">
                                <label for="eta-consigliata">Età Consigliata</label>
                                <input type="number" class="form-control" name="etaConsigliata" id="eta-consigliata" min="3" max="18" required>
                                <div class="invalid-feedback">Inserisci l'età minima consigliata</div>
                            </div>
                        </div>
                    </div>
                </div>`;
            break;
    }
    document.getElementById('dettagli-categoria').innerHTML = html;
}

function aggiungiCanzone() {
    numCanzoni = $("#canzoni").children().length + 1;
    html = `<div class="form-row">
                <div class="form-group col-8">
                    <label for="canzone-${numCanzoni}">Canzone ${numCanzoni}</label>
                    <input type="text" class="form-control" name="canzone-${numCanzoni}" id="canzone-${numCanzoni}" maxlength="45" required>
                    <div class="invalid-feedback">Inserisci il nome della canzone (max 45 caratteri)</div>
                </div>
                <div class="form-group col-4">
                    <label for="durata-${numCanzoni}">Durata</label>
                    <input type="time" step="1" class="form-control" name="durata-${numCanzoni}" id="durata-${numCanzoni}" required>
                    <div class="invalid-feedback">Inserisci la durata della canzone</div>
                </div>
            </div>
    `;
    $('#canzoni').append(html);
}

function rimuoviCanzone() {
    console.log(numCanzoni);
    $('#canzoni .form-row').last().remove();
}



