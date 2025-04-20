.PHONY: up down build re logs backend postgres clean returbo help

# 🚀 Démarrer tous les conteneurs (dev)
up:
	docker compose up -d

# ⛔ Stop + remove containers & volumes anonymes
down:
	docker compose down -v --remove-orphans

# 🔨 Rebuild (sans clean)
build:
	docker compose build

# 🔁 Rebuild + restart
re: down build up

# 🧼 Nettoyage complet (volumes, cache Docker)
clean: down
	docker system prune -af --volumes
	rm -rf ./postgresql/postgres-data/*

# 🔁 Rebuild total (⚠️ lent mais propre)
returbo: clean
	docker builder prune --all --force
	docker compose up --build -d

# 📜 Logs live
logs:
	docker compose logs -f

# 🔍 Logs backend uniquement
backend:
	docker compose logs -f backend

# 🔍 Logs postgres uniquement
postgres:
	docker compose logs -f postgres

# 🆘 Aide
help:
	@echo ""
	@echo "🛠️  Commandes disponibles :"
	@echo "  make up         → Démarrer tous les conteneurs"
	@echo "  make down       → Stopper & nettoyer les conteneurs"
	@echo "  make build      → Rebuilder les images Docker"
	@echo "  make re         → Rebuilder + restart"
	@echo "  make returbo    → Nettoyage complet + rebuild"
	@echo "  make logs       → Suivre tous les logs"
	@echo "  make backend    → Logs du backend"
	@echo "  make postgres   → Logs de PostgreSQL"
	@echo "  make clean      → Supprimer tout (images, volumes...)"
	@echo ""