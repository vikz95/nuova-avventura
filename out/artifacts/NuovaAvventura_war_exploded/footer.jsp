<%@ page contentType="text/html;charset=UTF-8" %>
<footer id="main-footer" class="bg-primary text-white text-center p-4">
    <div class="container">
        <div class="row">
            <div class="col">
                <blockquote class="blockquote text-center">
                    <p class="mb-0 text-success">&ldquo;...l'occhio non si sazia mai di vedere e l'orecchio non è mai
                        stanco di udire.&rdquo;</p>
                    <footer class="blockquote-footer"><cite title="Source Title">Ecclesiaste 1:8</cite></footer>
                </blockquote>
                <p>Copyright &copy; <span id="year"></span> Nuova Avventura, tutti i diritti riservati</p>
            </div>
        </div>
    </div>
</footer>

<jsp:include page="bootstrap-scripts.html"/>
<script>
    // Get the current year for the copyright
    $('#year').text(new Date().getFullYear());
</script>
</body>
</html>